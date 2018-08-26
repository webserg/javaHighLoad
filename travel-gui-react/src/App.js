import React, {Component} from 'react';
import './sortedMap.css';
import './App.css';
import './form.css';

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


let mapOfApps = new Map();
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


function EditButton(props) {
    if (isAdmin) {
        return (
            <div className="edit relative2" onClick={props.onClick}>
                ✖
            </div>
        );
    }
}

class SquareAppEntry extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            entry: props.entry,
            showPopup: false,
        };
    }

    togglePopup() {
        this.setState({
            showPopup: !this.state.showPopup
        });
    }

    render() {
        return (
            <div>
                <div className="border outer">{this.state.entry.name}
                    <EditButton onClick={this.togglePopup.bind(this)}/>
                </div>
                {this.state.showPopup ?
                    <AppForm closePopup={this.togglePopup.bind(this)} entry={this.state.entry}/>
                    : null
                }

            </div>
        );
    }

}


class AppForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {entry: props.entry};

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleSubmit = this.handleUpload.bind(this);
    }

    handleChange(event) {
        let newEntry = Object.assign({}, this.state.entry);
        const target = event.target;
        console.log(target.type)
        console.log(target.value)
        // const value = target.type === 'radio' ? !target.checked : target.value;
        newEntry[target.name] = target.value;
        this.setState({entry: newEntry});
    }

    handleUpload(event) {
    }

    handleSubmit(event) {
        alert('A name was submitted: ' + this.state.entry);
        // event.preventDefault();
    }

    render() {
        return (

            <div className="modal">
                <div id="divDialog" className="dialog boxshadow">
                    <div className="dialog-header">
                        <span class="close" onClick={this.props.closePopup}>&times;</span>
                        Add new app
                    </div>

                    <div className="dialog-content">
                        <div className="form">
                            <form method="post" onSubmit={this.handleSubmit}>
                                <ol>
                                    <li>
                                        <label for="name">Name:</label>
                                        <input type="text" name="name" id="name" value={this.state.entry.name}
                                               onChange={this.handleChange}/>
                                        <input class="upload" type="file" name="icon" accept="image/*"
                                               onChange={this.handleUpload}/>
                                    </li>
                                    <li>
                                        <label for="url">Link/URL:</label>
                                        <input type="text" name="url" id="url" value={this.state.entry.url}
                                               onChange={this.handleChange}/>
                                    </li>
                                    <li>
                                        <label for="description">Description:</label>
                                        <input type="text" name="description" id="description"
                                               value={this.state.entry.description}
                                               onChange={this.handleChange}/>
                                        <input class="upload" type="file" name="icon" accept="image/*"/>
                                    </li>
                                    <li>
                                        <label for="tags">Tags:</label>
                                        <input type="text" name="tags" id="tags" value={this.state.entry.tags}
                                               onChange={this.handleChange}/>
                                    </li>
                                    <li>
                                        <label>Featured / New:</label>
                                        <input id="radio" type="radio" name="featuredNew" value="yes"
                                               checked={this.state.entry.featuredNew === "yes"}
                                               onChange={this.handleChange}/>Yes
                                        <input id="radio2" type="radio" name="featuredNew" value="new"
                                               checked={this.state.entry.featuredNew === "new"}
                                               onChange={this.handleChange}/>New
                                    </li>
                                    <li>
                                        <div class="submit">
                                            <input type="button" value="Cancel" className="cancel"
                                                   onClick={this.props.closePopup}/>
                                            <input type="submit" value="Submit"/>
                                        </div>
                                    </li>
                                </ol>
                            </form>

                        </div>
                    </div>
                </div>
            </div>

        );
    }
}


class LetterList extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let i, j, temparray = [], chunk = 3;
        for (i = 0, j = this.props.list.length; i < j; i += chunk) {
            temparray.push(this.props.list.slice(i, i + chunk));
        }
        let trs = temparray.map((item, index) => {
            return (
                <tr key={index}>
                    {item.map(entry => {
                        return (<td><SquareAppEntry entry={entry}/></td>);
                    })}
                </tr>
            );
        });

        return (
            <table>
                {trs}
            </table>
        );
    }
}


class AlphabetList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            squares: mapOfApps,
        };
    }

    render() {
        let listItems = keys.map(k =>
            <LetterList list={mapOfApps.get(k)}/>
        );

        return (
            <div>
                {listItems}
            </div>
        )
    }
}

class App extends Component {
    render() {
        return (
            <div>
                <AlphabetList/>
            </div>
        );
    }
}

export default App;
