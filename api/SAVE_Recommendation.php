<?php
 if(isset($_POST['android_id']) 
	&& isset($_POST['nama_rec']) 
	&& isset($_POST['nama_papan']) 
	&& isset($_POST['posisi']) 
	&& isset($_POST['kerapatan']) 
	&& isset($_POST['kadar_air']) 
	&& isset($_POST['p_tebal']) 
	&& isset($_POST['d_serap'])
	&& isset($_POST['mod_elastis'])
	&& isset($_POST['mod_patah'])
	&& isset($_POST['sekrup'])
	&& isset($_POST['rekat_int'])
	&& isset($_POST['kasar'])
	&& isset($_POST['serbuk'])
	&& isset($_POST['minyak'])	 ){
  
  //NILAI VARIABEL
  $id_andro=$_POST['android_id'];
  $nama_rec=$_POST['nama_rec'];
  $nama_papan=$_POST['nama_papan'];
  $posisi=$_POST['posisi'];
  $kerapatan=$_POST['kerapatan'];
  $kadar_air=$_POST['kadar_air'];
  $p_tebal=$_POST['p_tebal'];
  $d_serap=$_POST['d_serap'];
  $mod_elastis=$_POST['mod_elastis'];
  $mod_patah=$_POST['mod_patah'];
  $sekrup=$_POST['sekrup'];
  $rekat_int=$_POST['rekat_int'];
  $kasar=$_POST['kasar'];
  $serbuk=$_POST['serbuk'];
  $minyak=$_POST['minyak'];
  
  //Import File Koneksi database
  require_once('connect.php');
  
  	$sql="INSERT INTO pp_tersimpan (android_id,nama_rec,nama_papan,posisi,Kerapatan,kadar_air,pengembangan_tebal,daya_serap_air,modulus_elastisitas,modulus_patah,kuat_pegang_sekrup,kuat_rekat_internal,partikel_kasar_permukaan,diameter_noda_serbuk,diameter_noda_minyak) 
	  VALUES ('$id_andro','$nama_rec','$nama_papan','$posisi','$kerapatan','$kadar_air','$p_tebal','$d_serap','$mod_elastis','$mod_patah','$sekrup','$rekat_int','$kasar','$serbuk','$minyak')";
	   
	if(mysqli_query($con,$sql)){
		$content=array('message'=>'SUKSES');
	} else{
		$content=array('id'=>$id_andro,'nama'=>$nama_rec,'papan'=>$nama_papan,'posisi'=>$posisi,'kerapatan'=>$kerapatan,'air'=>$kadar_air,'tebal'=>$p_tebal,
		'serap'=>$d_serap,'elastis'=>$mod_elastis,'patah'=>$mod_patah,'sekrup'=>$sekrup,'rekat'=>$rekat_int,'kasar'=>$kasar,'serbuk'=>$serbuk,'minyak'=>$minyak);
	}
  
  echo json_encode(array('result'=>$content));
  
  mysqli_close($con);
 }
?>