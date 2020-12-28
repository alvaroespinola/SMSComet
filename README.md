# SMSIngesoft

Original from https://github.com/juancrescente/SMSHub

Modified for my need, RO language, only one URL for PHP.

Added deveiceSecret in POST request.

Olso added "keepScreenOn" as a workaround "not sending SMS if screen is off"


### settings

you can customize the next settings directly in the application

#### Send SMS:
+ *Enable sending*: whether the app should read from the API and send messages
+ *send URL*: messages will be parsed from this URL, you return a JSON containing *message*, *number* and *id*
+ *interval*: the app will check whether there is an incoming message for sending each specific interval in minutes
+ *device id*: device id used in POST request
+ *device Secret*: device Secret used in POST request



### How sending SMSs works

1- The application connects at regular intervals to a URL

```
POST https://yourcustomurl.com/send_api
    deviceId: 1
    deviceSecret: 1234
    action: SEND
```

2- It should read a JSON containing *message*, *number* and *id*, or an empty response if there is nothing to send
```
[{ "message": "hola mundo!", "number": "3472664455", "messageId": "1" }]
```
Or Array of JSON
```
[{ "message": "hola mundo!", "number": "3472664455", "messageId": "1" },{ "message": "hola mundo!", "number": "3472664454", "messageId": "2" }]
```


3- The app will send the SMS *message* to *number*

4- Once sent (or failed) the app will notify the status to the status URL
```
POST https://yourcustomurl.com/send_api
    deviceId: 1
    deviceSecret: 1234
    messageId: 1
    status: SENT
    action: STATUS
```

5- Once delivered the app will notify the status to the status URL

```
POST https://yourcustomurl.com/send_api
    deviceId: 1
    deviceSecret: 1234
    messageId: 1
    status: DELIVERED
    action: STATUS
```

Possible _status_ values are: SENT, FAILED, DELIVERED (notice that it is unlikely but possible to get the DELIVERED update before the SENT update due to requests delay).


### How receiving SMSs works

1- Each time a SMS is received the app will notify the received URL
```
POST https://yourcustomurl.com/send_api
    deviceId: 1
    deviceSecret: 1234
    number: 3472556699
    message: Hello man!
    action: RECEIVED
```
