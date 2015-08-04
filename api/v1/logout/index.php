<?php

include '../libs/configer.php';
include '../libs/helper.php';
include '../libs/accesscontrol.php';
include '../sockets/socketConnector.php';
require '../sockets/class.PHPWebSocket.php';
$headers = apache_request_headers();
//print_r($headers);
$json = file_get_contents('php://input');
$obj = json_decode($json);

if (isset($headers['Authorization']) && isset($obj->{'logout_all_devices'})) {

    logout_from_all_devices(get_profile_id_from_oauth($headers['Authorization']), $headers['Authorization']);
} else if (isset($headers['Authorization']) && isset($obj->{'fingerprint'})) {
    $profile_id=get_profile_id_from_oauth($headers['Authorization']);
   // print_r($obj->{'fingerprint'});
    logout_specific_device($profile_id, $obj->{'fingerprint'});
} else if (isset($headers['Authorization'])) {

    logout($headers['Authorization']);
}
?>