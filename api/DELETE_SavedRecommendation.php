<?php
	$android_id=$_GET['id'];
	$nama_rec=$_GET['namaRec'];
		
	 //Importing database
	 require_once('connect.php');
	 
	 $sql = "DELETE FROM pp_tersimpan WHERE android_id='$android_id' AND nama_rec='$nama_rec'";
		
	 //$r = mysqli_query($con,$sql);
	 if (mysqli_query($con,$sql)){
		$content=array('result'=>'SUKSES');
	 } else{
		$content=array('result'=>'GAGAL');
	 }
	 
	 echo json_encode($content);
	
	mysqli_close($con);
?>