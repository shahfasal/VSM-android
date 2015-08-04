<?php
include '../libs/helper.php';
include '../libs/accesscontrol.php';
include '../libs/configer.php';
include '../libs/sql.php';
$headers = apache_request_headers();
$json = file_get_contents('php://input');
$obj = json_decode($json);
//print_r($json);exit;
if(isset($headers['X-Console-Key']) && isset($headers['Authorization'])){
   $secret =$headers['secret_key'];
   $id = $obj->{"course_id"};
   $course_meta = $obj->{"course_meta"};
   $oauth = $headers['Authorization'];
  $course_meta =  json_encode($course_meta);
   $profile_id =get_profile_id_from_secret($secret,$oauth);
   if ($profile_id) {
        $sql = "UPDATE course SET json_dump= '$course_meta' where course_id=$id";
        $result = $conn->query($sql);
        $result_array = array('status' => 'Success',
        'message' => 'Updated Successfully');

    print_r(json_encode($result_array));
   }else{
        header($_SERVER["SERVER_PROTOCOL"]." ".$GLOBALS['status_notfound']);
        $result_array = array('status' => 'Error',
            'message' => 'Course not updated');
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
