let apps = [
    {
        id:1,
        name: "first",
        url: "first.com",
        description:" lorem ipsum lorem ipsum",
        tags:"news search",
        featuredNew:"yes"
    },
    {
        id:2,
        name: "ffirst",
        url: "ffirst.com",
        description:" lorem ipsum lorem ipsum",
        tags:"news search",
        featuredNew:"new"
    },
    {
        id:3,
        name: "abc",
        url: "abc.com",
        description:" lorem ipsum lorem ipsum",
        tags:"news search",
        featuredNew:"yes"
    },
    {
        id:4,
        name: "abcd",
        url: "abcd.com",
        description:" lorem ipsum lorem ipsum",
        tags:"news search",
        featuredNew:"new"
    },
    {
        id:5,
        name: "abcde",
        url: "abcde.com",
        description:" lorem ipsum lorem ipsum",
        tags:"news search",
        featuredNew:"new"
    },
    {
        id:6,
        name: "wwwfirst",
        url: "www.wwwwfirst.com",
        description:" lorem ipsum lorem ipsum",
        tags:"news search",
        featuredNew:"new"
    },
    {
        id:7,
        name: "zzzwwwfirst",
        url: "www.zzzwwwwfirst.com",
        description:" lorem ipsum lorem ipsum",
        tags:"news search",
        featuredNew:"new"
    }
];

mapOfApps = new Map();
apps.forEach(app => {
    let firstChar = app.name.charAt(0);
    if (!mapOfApps.get(firstChar)) {
        mapOfApps.set(firstChar, []);
    }
    mapOfApps.get(firstChar).push(app);

});
let keys = Array.from(mapOfApps.keys());
keys.sort(function (a, b) {
    return a > b
});

console.log(keys);

keys.forEach(k => {
    let listOfApps = mapOfApps.get(k);
    listOfApps.forEach(l => {
        console.log(l)
    });
    listOfApps.map(l => {
        console.log(l)
    })

});

let isAdmin = true;
