<?php
include '../libs/helper.php';
include '../libs/accesscontrol.php';
include '../libs/configer.php';
include '../libs/sql.php';
$headers = apache_request_headers();
$json = file_get_contents('php://input');
$obj = json_decode($json);
//print_r($obj);
if(isset($headers['secret_key'])&& isset($_GET['course_id'])&& isset($headers['Authorization'])){
   $oauth = $headers['Authorization'];
   $secret =$headers['secret_key'];
   $id = $_GET['course_id'];
   $profile_id =get_profile_id_from_secret($secret,$oauth);
   if ($profile_id) {
        $sql = "DELETE FROM course where course_id=$id";
        $result = $conn->query($sql);
         $result_array = array('status' => 'Success',
        'message' => 'Deleted Successfully');

    print_r(json_encode($result_array));
    }else{
         header($_SERVER["SERVER_PROTOCOL"]." ".$GLOBALS['status_notfound']);
        $result_array = array('status' => 'Error',
            'message' => 'Course could not be deleted');

        print_r(json_encode($result_array));
    }
}else {
    header($_SERVER["SERVER_PROTOCOL"]." ".$GLOBALS['bad_request']);
    $result_array = array('status' => 'Error',
        'message' => 'Method not allowed');

    print_r(json_encode($result_array));
}
?>
