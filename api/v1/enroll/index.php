<?php
include '../libs/helper.php';
include '../libs/accesscontrol.php';
include '../libs/configer.php';
include '../libs/sql.php';
$headers = apache_request_headers();
$json = file_get_contents('php://input');
$obj = json_decode($json);
if (isset($obj->{"course_id"}) && isset($headers['Authorization']) && !empty($headers['Authorization'])){
        $oauth_key= $headers['Authorization'];
        $profile_id=get_profile_id_from_oauth($oauth_key);
        if($profile_id){
        $course_id=$obj->{"course_id"};
        $sql = "insert into enroll(course_id,profile_id) values($course_id,'$profile_id')";
        $result = $conn->query($sql);
       header($_SERVER["SERVER_PROTOCOL"]." ".$GLOBALS['status_found']);
        $result_array = array('status' => 'Success',
            'message' => 'Enrolled Successfully');

        print_r(json_encode($result_array));

        }else{
            header($_SERVER["SERVER_PROTOCOL"]." ".$GLOBALS['status_notfound']);
        $result_array = array('status' => 'Error',
            'message' => 'Enroll Unsuccessful');

        print_r(json_encode($result_array));
        }
    
        
}else {
    header($_SERVER["SERVER_PROTOCOL"]." ".$GLOBALS['bad_request']);
    $result_array = array('status' => 'Error',
        'message' => 'Method not allowed');

    print_r(json_encode($result_array));

        
}
?>
