const { WebcastPushConnection } = require('tiktok-live-connector');
const express = require('express');

var users = {

}

var usersRewards = {

}

function registerAll(obj) {

    obj.tt.on('streamEnd', (actionId) => {
        obj.tt.disconnect();
        obj = null;
    })

    obj.tt.on('chat', data => {
        obj.messages[ Date.now() ] = {
            uniqueId: data.uniqueId,
            userId: data.userId,
            type: "message",
            data: {
                comment: data.comment,
            }
        }

        console.log(usersRewards, tiktokLiveConnection.userId, tiktokLiveConnection)
    })

// And here we receive gifts sent to the streamer
    obj.tt.on('gift', data => {
        // console.log(`${data.uniqueId} (userId:${data.userId}) sends ${data.giftId} ${data.giftName}`);
        obj.gift[ Date.now() ] = {
            uniqueId: data.uniqueId,
            userId: data.userId,
            type: "gift",
            data: {
                giftId: data.giftId,
                giftName: data.giftName
            }
        }
    })

    obj.tt.on('like', data => {
        //console.log(`${data.uniqueId} (userId:${data.userId}) likes`);
        obj.data[ Date.now() ] = {
            uniqueId: data.uniqueId,
            userId: data.userId,
            type: "like"
        }
    });

    obj.tt.on('follow', data => {
        obj.data[ Date.now() ] = {
            uniqueId: data.uniqueId,
            userId: data.userId,
            type: "follow"
        }
    });
}




const app = express()
const port = 1975

app.get('/get/:name', (req, res) => {
    if (users[req.params.name].data == undefined) {
        res.send("Error: User not found");
        return;
    }

    res.send(users[req.params.name].data || {});
    users[req.params.name].data = {};
})

app.get('/gift/:name', (req, res) => {
    if (users[req.params.name].gift == undefined) {
        res.send("Error: User not found");
        return;
    }

    res.send(users[req.params.name].gift || {"error": "No gifts"});
    users[req.params.name].gift = {};
})

app.get('/messages/:name', (req, res) => {
    if (users[req.params.name].messages == undefined) {
        res.send("Error: User not found");
        return;
    }

    res.send(users[req.params.name].messages || {});
    users[req.params.name].messages = {};
})

app.get('/start/:name', (req, res) => {

    let tiktokUsername = req.params.name;

    if (users[tiktokUsername] != undefined && users[tiktokUsername].tt != undefined) {
        res.send(`Error: connection already started, type /stop/${tiktokUsername} to stop it`);
        return;
    }

    users[tiktokUsername] = {}
    users[tiktokUsername].tt = new WebcastPushConnection(tiktokUsername);
    users[tiktokUsername].name = tiktokUsername;
    users[tiktokUsername].data = {};
    users[tiktokUsername].gift = {};
    users[tiktokUsername].messages = {};

    users[tiktokUsername].tt.connect().then(state => {
        console.info(`Connected to roomId ${state.roomId}`);
        registerAll(users[tiktokUsername]);
        res.send("ok");
    }).catch(err => {
        console.error('Failed to connect', err);
        users[tiktokUsername].tt = undefined;
        res.send(`Error: try again [${err}]`);
    })
});

app.get('/stop/:name', (req, res) => {
    let tiktokUsername = req.params.name;

    if (users[tiktokUsername] == null) {
        res.send(`Error: connection not started or disconnected, type /start/${tiktokUsername} to start it`);
        return;
    }

    users[tiktokUsername].tt.disconnect();
    users[tiktokUsername] = null;
    res.send("ok");
});

app.listen(port, () => {
    console.log(`Web app listening on port ${port}`)
})
