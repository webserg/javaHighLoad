package com.webserg.zookeeper.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class Manager {
        private static ZooKeeper zkeeper;
        private static ZKConnection zkConnection;

        public Manager() {
            initialize();
        }

        /** * Initialize connection */
        private void initialize() {
            try {
                zkConnection = new ZKConnection();
                zkeeper = zkConnection.connect("localhost");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        public void closeConnection() {
            try {
                zkConnection.close();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        public void create(String path, byte[] data) throws KeeperException, InterruptedException {
            zkeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        public Object getZNodeData(String path, boolean watchFlag) {
            try {
                byte[] b = null;
                b = zkeeper.getData(path, null, null);
                String data = new String(b, "UTF-8");
                System.out.println(data);
                return data;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        public void update(String path, byte[] data) throws KeeperException, InterruptedException {
            int version = zkeeper.exists(path, true)
                    .getVersion();
            zkeeper.setData(path, data, version);
        }


    public static void main(String[] args) {
            Manager manager = new Manager();
            Object data = manager.getZNodeData("/zk_test", true);
            System.out.println(data);
            manager.closeConnection();

    }
}
