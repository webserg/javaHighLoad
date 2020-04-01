import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TxHandler {

    private final UTXOPool pool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        this.pool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     * values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        double totalInput = 0.0;
        Set<UTXO> utxosAvail = new HashSet<>();
        for (int i = 0; i < tx.getInputs().size(); i++) {
            Transaction.Input in = tx.getInput(i);
            UTXO ut = new UTXO(in.prevTxHash, in.outputIndex);
            Transaction.Output out = pool.getTxOutput(ut);
            if (out == null)
                return false;// satisfying condition #1
            if (!Crypto.verifySignature(out.address, tx.getRawDataToSign(i), in.signature))//satisfying cond #2
                return false;
            if (!utxosAvail.add(ut)) //satisfying condition #3
                return false;
            totalInput += out.value;
        }
        double totalOutput = 0.0;
        ArrayList<Transaction.Output> txOutputs = tx.getOutputs();
        for (Transaction.Output op : txOutputs) { // satisfying condition #4
            if (op.value < 0)
                return false;
            totalOutput += op.value;
        }
        return (totalInput >= totalOutput);//---- satisfying condition #5
    }


    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    private boolean inPool(Transaction tx) {
        ArrayList<Transaction.Input> inputs = tx.getInputs();
        Transaction.Input in;
        UTXO ut;
        for (int i = 0; i < inputs.size(); i++) {
            in = inputs.get(i);
            ut = new UTXO(in.prevTxHash, in.outputIndex);
            if (!pool.contains(ut))
                return false;
        }
        return true;
    }

    private void updatePool(Transaction tx) {
        for (int i = 0; i < tx.getInputs().size(); i++) {
            Transaction.Input in = tx.getInput(i);
            pool.removeUTXO(new UTXO(in.prevTxHash, in.outputIndex));
        }
        for (int i = 0; i < tx.getOutputs().size(); i++) {
            Transaction.Output out = tx.getOutput(i);
            pool.addUTXO(new UTXO(tx.getHash(), i), out);
        }
    }

    // do not change actual utxo pool because maintained a separate copy
    public Transaction[] handleTxs(Transaction[] allTx) {
        Transaction[] stuckTxs = new Transaction[allTx.length];
        for (int i = 0; i < allTx.length; i++)
            stuckTxs[i] = allTx[i];
        Transaction[] tempTxs = new Transaction[allTx.length];
        Transaction[] successTxs = new Transaction[allTx.length];
        int tempCounter = 0, successCounter = 0;
        int stuckSize = allTx.length;
        while (true) {
            boolean change = false;
            tempCounter = 0;
            for (int i = 0; i < stuckSize; i++) {
                if (inPool(stuckTxs[i])) {
                    if (isValidTx(stuckTxs[i])) {
                        change = true;
                        updatePool(stuckTxs[i]);
                        successTxs[successCounter++] = stuckTxs[i];
                    }
                } else {
                    tempTxs[tempCounter++] = stuckTxs[i];
                }
            }
            if (change) {
                for (int i = 0; i < tempCounter; i++) {
                    stuckTxs[i] = tempTxs[i];
                }
                stuckSize = tempCounter;
            } else {
                break;
            }
        }
        Transaction[] result = new Transaction[successCounter];
        for (int i = 0; i < successCounter; i++)
            result[i] = successTxs[i];
        return result;
    }

}
