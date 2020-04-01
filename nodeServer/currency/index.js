const canadienDollar=0.91
function roundTwo(amount){
    return Math.round(amount * 100) / 100
}

exports.canadienToUS = canadien => roundTwo(canadien * canadienDollar)
exports.UStocanadienT = us=> roundTwo(ua / canadienDollar)