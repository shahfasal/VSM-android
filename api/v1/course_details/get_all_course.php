<?php

include '../libs/helper.php';
include '../libs/accesscontrol.php';
include '../libs/configer.php';
include '../libs/sql.php';
$headers = apache_request_headers();
$json = file_get_contents('php://input');
if (!isset($headers['X-Console-Key']) && isset($headers['Authorization'])) {
//    $secret = $headers['X-Console-Key'];
    $oauth = $headers['Authorization'];
///    $profile_id = get_profile_id_from_secret($secret, $oauth);
    $profile_id = get_profile_id_from_oauth($oauth);
    if ($profile_id) {
        $sql = "SELECT * FROM " . $dbname . ".course";
        $result = $conn->query($sql);

        $all_courses = array();
        while ($row = $result->fetch_assoc()) {
            $myarray = array();
            $myarray['course_id'] = $row['course_id'];
            $decoded_json = json_decode($row['json_dump']);
            $myarray['details'] = $decoded_json->{'description'};
            $myarray['is_admin'] = false;
            if (check_enroll($row['course_id'], $profile_id)) {
                $myarray['is_enroll'] = true;
            } else {
                $myarray['is_enroll'] = false;
            }
            $all_courses[] = $myarray;
        }

        $result_array = array('status' => 'Success',
            'message' => 'Courses found', 'all_courses' => $all_courses);

        print_r(json_encode($result_array));
    } else {
        header($_SERVER["SERVER_PROTOCOL"] . " " . $GLOBALS['status_notfound']);
        $result_array = array('status' => 'Failure',
            'message' => 'Unsuccessful could not fetch the courses');

        print_r(json_encode($result_array));
    }
} else if (isset($headers['X-Console-Key']) && isset($headers['Authorization'])) {
    $secret = $headers['X-Console-Key'];
    $oauth = $headers['Authorization'];
    $profile_id = get_profile_id_from_secret($secret, $oauth);
    if ($profile_id) {
        $sql = "SELECT * FROM " . $dbname . ".course";
        $result = $conn->query($sql);

        $all_courses = array();
        while ($row = $result->fetch_assoc()) {
            $myarray = array();
            $myarray['course_id'] = $row['course_id'];
            $decoded_json = json_decode($row['json_dump']);
            $myarray['details'] = $decoded_json->{'description'};
            $myarray['is_admin'] = true;
            if (check_enroll($row['course_id'], $profile_id)) {
                $myarray['is_enroll'] = true;
            } else {
                $myarray['is_enroll'] = false;
            }
            $all_courses[] = $myarray;
        }

        $result_array = array('status' => 'Success',
            'message' => 'Courses found', 'all_courses' => $all_courses);

        print_r(json_encode($result_array));
    } else {
        header($_SERVER["SERVER_PROTOCOL"] . " " . $GLOBALS['status_notfound']);
        $result_array = array('status' => 'Failure',
            'message' => 'Unsuccessful could not fetch the courses');

        print_r(json_encode($result_array));
    }
} else {
    header($_SERVER["SERVER_PROTOCOL"] . " " . $GLOBALS['bad_request']);
    $result_array = array('status' => 'Error',
        'message' => 'Method not allowed');

    print_r(json_encode($result_array));
}
?>
