<?php
include '../libs/helper.php';
include '../libs/accesscontrol.php';
include '../libs/configer.php';
include '../libs/sql.php';
$headers = apache_request_headers();
$json = file_get_contents('php://input');
if(isset($headers['X-Console-Key']) && isset($headers['Authorization'])){
   $secret =$headers['X-Console-Key'];
   $oauth = $headers['Authorization'];
   $profile_id =get_profile_id_from_secret($secret,$oauth);
   if ($profile_id) {
        $sql = "insert into " . $dbname . ".course(json_dump) value ('$json')";
        $result = $conn->query($sql);
         $result_array = array('status' => 'Success',
        'message' => 'Posted Successfully');

    print_r(json_encode($result_array));
    }else{
         header($_SERVER["SERVER_PROTOCOL"]." ".$GLOBALS['status_notfound']);
        $result_array = array('status' => 'Failure',
            'message' => 'Unsuccessful Post');

        print_r(json_encode($result_array));
    }
}
else {
    header($_SERVER["SERVER_PROTOCOL"]." ".$GLOBALS['bad_request']);
    $result_array = array('status' => 'Error',
        'message' => 'Method not allowed');

    print_r(json_encode($result_array));
}
?>
