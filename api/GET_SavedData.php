<?php
		$android_id=$_GET['id'];
		$nama_rec=$_GET['namaRec'];
		
		 //Importing database
		 require_once('connect.php');
		 
		 $sql = "SELECT * FROM pp_tersimpan WHERE android_id='$android_id' AND  nama_rec='$nama_rec' ORDER BY posisi";
		
		 $r = mysqli_query($con,$sql);
		 
		 $content=mysqli_fetch_all($r,MYSQLI_ASSOC);
		 
		 echo json_encode(array('result'=>$content));
		
		mysqli_close($con);
?>