<?php
		 //Importing database
		 require_once('connect.php');
		 
		 $kr = "SELECT nama_kriteria, bobot FROM kriteria ORDER BY id_kriteria";
		 $sf = "SELECT nama_sub, bobot FROM sub_kriteria WHERE id_kriteria='K1' ORDER BY id_sub";
		 $sm = "SELECT nama_sub, bobot FROM sub_kriteria WHERE id_kriteria='K2'";
		 $mp = "SELECT nama_sub, bobot FROM sub_kriteria WHERE id_kriteria='K3'";
		
		 $q = mysqli_query($con,$kr);
		 $r = mysqli_query($con,$sf);
		 $s = mysqli_query($con,$sm);
		 $t = mysqli_query($con,$mp);
		 
		 $kriteria=mysqli_fetch_all($q,MYSQLI_ASSOC);
		 $sifatFisis=mysqli_fetch_all($r,MYSQLI_ASSOC);
		 $sifatMekanis=mysqli_fetch_all($s,MYSQLI_ASSOC);
		 $mutuPenampilan=mysqli_fetch_all($t,MYSQLI_ASSOC);
		 
		 echo json_encode(array('KRITERIA'=>$kriteria,'SUB-KRITERIA 1'=>(array('sifat_fisis'=>$sifatFisis)),'SUB-KRITERIA 2'=>(array('sifat_mekanis'=>$sifatMekanis)),'SUB-KRITERIA 3'=>(array('mutu_penampilan'=>$mutuPenampilan))));
		
		mysqli_close($con);
?>