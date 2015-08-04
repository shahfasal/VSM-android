<?php
include '../libs/helper.php';
include '../libs/accesscontrol.php';
include '../libs/configer.php';
include '../libs/sql.php';
$headers = apache_request_headers();
$json = file_get_contents('php://input');
$obj = json_decode($json);

if (isset($_GET['course_id'])&& isset($headers['Authorization']) && !empty($headers['Authorization'])){
        $oauth_key= $headers['Authorization'];
        $profile_id=get_profile_id_from_oauth($oauth_key);
        if($profile_id){
        $id=$_GET['course_id'];
        $sql = "SELECT json_dump FROM " . $dbname . ".course where course_id=$id";
        $result = $conn->query($sql);
        if ($result->num_rows > 0) {

            $row = $result->fetch_assoc();
            $json = $row['json_dump'];
            print_r($json);
}

        }else{
            header($_SERVER["SERVER_PROTOCOL"]." ".$GLOBALS['status_notfound']);
        $result_array = array('status' => 'Error',
            'message' => 'Course details not found');

        print_r(json_encode($result_array));
        }
    
        
}else {
    header($_SERVER["SERVER_PROTOCOL"]." ".$GLOBALS['bad_request']);
    $result_array = array('status' => 'Error',
        'message' => 'Method not allowed');

    print_r(json_encode($result_array));
}
?>
