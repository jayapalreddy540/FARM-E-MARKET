const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.addProduct = functions.firestore
    .document('data/{userId}/products/{productId}')
    .onCreate((snap, context) => {
        const newValue = snap.data();
        const name=newValue.name;
        const category = newValue.category;
        const image=newValue.image;

//const getInstanceIdPromise = functions.firestore.ref(`/app/tokens/tokenArray`).once('value');
    let getInstanceIds=[];

    const v= functions.firestore.document('app/tokens')
    .get()
    .then(doc => {
      if (!(doc && doc.exists)) {
      console.log("document not found");
      }
      const data = doc.data();
      getInstanceIds=data.tokenArray;
      return res.status(200).send(data);
    }).catch(err => {
      console.error(err);
      return res.status(404).send({ error: 'Unable to retrieve the document' });
    });

// Create a notification
    const payload = {
        notification: {
            title:"FARM-E-MARKET",
            body: "New Product Added : "+name,
            image:image,
            sound: "default"
        },
    };

  //Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };


    return admin.messaging().sendToDevice(getInstanceIds, payload, options)
    .then(function(response){
    return console.log("notification sent successfully");
    })
    .catch(function(error){
    return console.log("Error sending Notification : "+error);
    });
});