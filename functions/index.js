const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.firestore.document('data/{userId}/products/{productId}').onCreate((change,context) => {
    const name = context.params.name;
    const category = context.params.category;
    const image=context.params.image;
    console.log("name : "+name);


    return admin.firestore().collection("app").doc("tokens").get().then(queryResult => {
    const token=queryResult.data().tokenArray;

    console.log("tokenId: "+token);

            const notificationContent = {
                notification: {
                    title: "FARM-E-MARKET",
                    body: "New product added : "+name,
                    icon: "default",
                    sound : "default"
                }
              };
        return admin.messaging().sendToDevice(token, notificationContent).then(result => {
        console.log("Notification sent!");
        return null;
            }).catch(err=>{
              console.error(err);              
            });
    }).catch(err=>{
      return console.error(err);
    });
});