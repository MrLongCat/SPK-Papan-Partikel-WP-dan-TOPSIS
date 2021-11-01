<?php
		$android_id=$_GET['id'];
		
		 //Importing database
		 require_once('connect.php');
		 
		 $sql = "SELECT nama_rec FROM pp_tersimpan WHERE android_id='$android_id' GROUP BY nama_rec ORDER BY id_rec";
		
		 $r = mysqli_query($con,$sql);
		 
		 $content=mysqli_fetch_all($r,MYSQLI_ASSOC);
		 
		 echo json_encode(array('result'=>$content));
		
		mysqli_close($con);
?>