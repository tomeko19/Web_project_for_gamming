<?php
  include("connect.php");
    
    if((isset($_POST['name']) && !empty($_POST['name']))
    && (isset($_POST['email']) && !empty($_POST['email']))
    && (isset($_POST['subject']) && !empty($_POST['subject']))){
        $name = $_POST['name'];
        $email = $_POST['email'];
        $subject = $_POST['subject'];
        $message = $_POST['msg'];
        
        // compilo un messaggio combinando i dati recuperati dal form
        $testo = "Nome: " . $name . "\n"
               . "Email: " . $email . "\n"
               . "Sogetto: " . $subject . "\n"
               . "Messaggio:\n" . $message;
        
        // uso la funzione mail di PHP per inviare questi dati al mio indirizzo di posta
        //mail('chaenel2010@yahoo.com', 'Messaggio dal mio sito web', $testo);
        //mail('tchatotomeko@yahoo.com', 'Messaggio dal mio sito web', $testo);
        //$headers = "From : " . $email;
        
        if( mail('chaenel2010@yahoo.com', 'Messaggio dal mio sito web', $testo) && mail('tchatotomeko@yahoo.com', 'Messaggio dal mio sito web', $testo) ){
            echo "E-Mail Sent successfully, we will get back to you soon.";
        }
    }

    $query = "INSERT INTO contact (name, email, subject, message) VALUES ('$name', '$email', '$subject', '$message')";
   /*$connect is the variable from connect.php*/
   echo "$query";
    $stmt= $connect->prepare($query); 
  $stmt->execute();
?>