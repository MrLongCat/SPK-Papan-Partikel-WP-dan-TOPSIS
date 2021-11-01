<?php
  //NILAI VARIABEL
  $id_andro=$_GET['id'];
  $nama_rec=$_GET['nama_rec'];
  
  //Import File Koneksi database
  require_once('connect.php');
  
  $check = "SELECT * FROM pp_tersimpan WHERE (android_id='$id_andro' AND nama_rec='$nama_rec')";
  $r=mysqli_query($con,$check);
  $r_row=mysqli_num_rows($r);
  if($r_row<1){
		$content=array('message'=>'KOSONG');
  } else{
		$content=array('message'=>'DUPLICATE');
  }
  
  echo json_encode(array('result'=>$content));
  
  mysqli_close($con);
?>