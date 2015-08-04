#! C:/xampp/php/php
<?php
include '../libs/helper.php';
include '../libs/accesscontrol.php';
include '../libs/configer.php';
// prevent the server from timing out
set_time_limit(0);

// include the web sockets server script (the server is started at the far bottom of this file)
require 'class.PHPWebSocket.php';

// when a client sends data to the server
function wsOnMessage($clientID, $message, $messageLength, $binary) {
    global $Server;
    $ip = long2ip($Server->wsClients[$clientID][6]);



    if ($message == "quit") {

//Send the message to everyone but the person who said it
        foreach ($Server->wsClients as $id => $client) {
            if ($id != $clientID) {
                if ($Server->{"userData"}["$id"] == $Server->{"userData"}["$clientID"]) {
                    $Server->wsSend($id, json_decode($Server->{"deviceData"}["$clientID"])->{"device-details"});
                }
            }
        }
        $Server->wsRemoveClient($clientID);
    } else if ($message == "show") {


//        $Server->wsSend($clientID, ">oauth::" . $Server->{"data"}["$clientID"]." profile id::".$Server->{"userData"}["$clientID"]).
//            " device::".$Server->{"deviceData"}["$clientID"];
        $logout = array("oauth" => json_decode($Server->{"deviceData"}["$clientID"])->{"device-details"});
        $Server->wsSend($clientID, json_encode($logout));
    } else if ($message == "test") {
        $Server->log("message froma  server");
    } else
    if ($message == "BROADCAST_LOGIN") {
        $Server->log($message);
        foreach ($Server->wsClients as $id => $client) {
            if ($id != $clientID) {
                if ($Server->{"userData"}["$id"] == $Server->{"userData"}["$clientID"]) {
                    $myres = array("LOGIN" => json_decode($Server->{"deviceData"}["$clientID"])->{"device-details"});
                    $Server->wsSend($id, json_encode($myres));
                }
            }
        }
    } else
    if ($message == "BROADCAST_LOGOUT") {
        $Server->log($message);
        foreach ($Server->wsClients as $id => $client) {
            if ($id != $clientID) {
                if ($Server->{"userData"}["$id"] == $Server->{"userData"}["$clientID"]) {
                    $myres = array("LOGOUT" => json_decode($Server->{"deviceData"}["$clientID"])->{"device-details"});
                    $Server->wsSend($id, json_encode($myres));
                }
            }
        }
    } else
    if ($message == "BROADCAST_LOGOUT_ALL") {
        $Server->log($message);
        foreach ($Server->wsClients as $id => $client) {

            if ($Server->{"userData"}["$id"] == $Server->{"userData"}["$clientID"]) {
                $myres = array("LOGOUT_ALL" => "logout from your device please!");
                $Server->wsSend($id, json_encode($myres));
            }
        }
    } else {
        $Server->{"data"}["$clientID"] = $message;
        $myres = array("oauth" => $Server->{"data"}["$clientID"]);
        $Server->wsSend($clientID, json_encode($myres));
        $Server->{"flags"}["$clientID"] = "false";
        /*
         * fetch the user data and store
         */
        $Server->{"userData"}["$clientID"] = get_profile_id_from_oauth($Server->{"data"}["$clientID"]);
        $Server->{"deviceData"}["$clientID"] = get_device_data($Server->{"userData"}["$clientID"], $Server->{"data"}["$clientID"]);
    }

//    else
//    {
//        $Server->log("some ass sent this".$message);
//      $Server->wsSend($clientID, "get lost:");  
//    }
}

// when a cli
// ent connects
function wsOnOpen($clientID) {
    global $Server;

    $ip = long2ip($Server->wsClients[$clientID][6]);

    $Server->log("$ip ($clientID) has connected.");

//Send a join notice to everyone but the person who joined
//    foreach ($Server->wsClients as $id => $client)
//        if ($id != $clientID) {
//            $Server->wsSend($id, "$clientID ($ip) logged in.");
//        }
}

// when a client closes or lost connection
function wsOnClose($clientID, $status) {
    global $Server;
    $ip = long2ip($Server->wsClients[$clientID][6]);

    $Server->log("$ip ($clientID) has disconnected.");
//    foreach ($Server->wsClients as $id => $client) {
//            if ($id != $clientID) {
//                if ($Server->{"userData"}["$id"] == $Server->{"userData"}["$clientID"]) {
//                    $myres=array("LOGOUT"=>json_decode($Server->{"deviceData"}["$clientID"])->{"device-details"});
//                    $Server->wsSend($id, json_encode($myres));
//                }
//            }
//        }
}

// start the server
$Server = new PHPWebSocket();
$Server->bind('message', 'wsOnMessage');
$Server->bind('open', 'wsOnOpen');
$Server->bind('close', 'wsOnClose');
// for other computers to connect, you will probably need to change this to your LAN IP or external IP,
// alternatively use: gethostbyaddr(gethostbyname($_SERVER['SERVER_NAME']))
$Server->wsStartServer('192.168.1.4', 9300);


//$GLOBALS['myserver'] = $Server;
?>