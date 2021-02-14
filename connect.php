<?php 
 
  // dati di connessione al mio database MyphpAdmin
   $dsn = "mysql:dbname=my_pweb1819moozeunacb; host=localhost";
   $username = "pweb1819moozeunacb";	
   $password = "QMJr4ENEsqGb";
	
	// connessione al DB utilizzando PDO
	try{
            $connect = new PDO($dsn,$username,$password);
            $connect->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);	
	}

	// verifica su eventuali errori di connessione
	catch(PDOException $e){
            echo "Connection failed!".$e-> getMessage();
        }
?>
