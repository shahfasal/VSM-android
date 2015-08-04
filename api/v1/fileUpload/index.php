<?php

include '../libs/helper.php';
include '../libs/accesscontrol.php';
$headers = apache_request_headers();
//if (isset($_FILES) && isset($headers['oauth_key'])) {
//    file_upload($headers['oauth_key']);
//}
if (isset($_FILES)) {
    my_file_upload($headers['Authorization'],$_POST['course_id'],$_POST['type']);
}
?>


<?php

//echo $_FILES['file']['name'];
//echo $_FILES['file']['size'] . "bytes";
//echo $_FILES['file']['type'];
?>
