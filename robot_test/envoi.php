<?php
session_start();
$notify = 0;
function VerifierAdresseMail($mail){
$Syntaxe='#^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,6}$#';
if(preg_match($Syntaxe,$mail)){
return true;
}else{
return false;
}
}


/* PetitClean($var,$lg) */
/* $var la varible à traiter */
/* la longueur de sortie */  

function PetitClean($var,$lg){
$var=strip_tags($var);
  /* troncature on va pas me poster un roman (-: */
  if(strlen($var)>$lg){
  $var = substr($var, 0, $lg);
  $last_space = strrpos($var, " ");
  $var = substr($var, 0, $last_space);
  }else{
  $lg=0;
  } 
return $var;
}
    
$error=NULL;

if(isset($_POST['name']) && !empty($_POST['name'])){
$nom=$_POST['name'];$error=NULL;
//filtrage 
$nom=PetitClean($nom,30); /*30 caractères maxi*/
}else{
echo $error='<h3 align="center">il nome e vuoto - <a href="javascript:history.back();">TORNA AL FORMULARIO</a></h3>';exit;
}

if(isset($_POST['email']) && !empty($_POST['email'])){
$mail=$_POST['email'];$error=NULL;$mail=htmlentities($mail);
//filtrage
$mail=PetitClean($mail,60);
}else{
echo $error='<h3 align="center">Email vuoto - <a href="javascript:history.back();">TORNA AL FORMULARIO</a></h3>';exit;
}

if(isset($_POST['subject']) && !empty($_POST['subject'])){
$objet=$_POST['subject'];$error=NULL;
//filtrage
$objet=PetitClean($objet,100);
}else{
echo $error='<h3 align="center">Soggetto vuoto - <a href="javascript:history.back();">TORNA AL FORMULARIO</a></h3>';exit;
}

if(isset($_POST['msg']) && !empty($_POST['msg'])){
$message=$_POST['msg'];$error=NULL;
//filtrage
$message=PetitClean($message,300);
}else{
echo $error='<h3 align="center">Messaggio vuoto - <a href="javascript:history.back();">TORNA AL FORMULARIO</a></h3>';exit;
}

if(VerifierAdresseMail($mail)){
//echo 'mail ok';
}else{
echo $error='<h3 align="center">Email vuoto n\'est pas valide - <a href="javascript:history.back();">TORNA AL FORMULARIO</a></h3>';exit;
}

//insert in database and sent mail tu administrator

include("../connect.php");
    
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
        mail('chaenel2010@yahoo.com', 'Messaggio dal mio sito web', $testo);
        mail('tchatotomeko@yahoo.com', 'Messaggio dal mio sito web', $testo);
        //$headers = "From : " . $email;
        
        /*if( mail('chaenel2010@yahoo.com', 'Messaggio dal mio sito web', $testo) && mail('tchatotomeko@yahoo.com', 'Messaggio dal mio sito web', $testo) ){
            echo "Tutto OK";
        }*/
    }

    $query = "INSERT INTO contact (name, email, subject, message) VALUES ('$name', '$email', '$subject', '$message')";
   /*$connect is the variable from connect.php*/
   //echo "$query";
    $stmt= $connect->prepare($query); 
    $stmt->execute();
/*end inserting to database and sending mail*/


if($_SERVER['REQUEST_METHOD']==='POST' && isset($_POST['code']) && !empty($_POST['code']) && $_POST['code']===$_SESSION['verif']){ 

/*un mail, un enregistrement mysql, une ouverture de fichier ... un traitement */
$destinataire="xxxxxxxxxxxxxxx@free.fr";  /*ICI LE MAIL QUI RECEPTIONNE*/
$subject=$objet;
$body=$message;

/*format du mail*/
$headers = "MIME-Version: 1.0\r\n";
$headers .= "Content-type: text/plain; charset=iso-8859-1\r\n";
/*ici on détermine l'expediteur et l'adresse de réponse*/
$headers .= "From: $nom <$mail>\r\nReply-to : $nom <$mail>\nX-Mailer:PHP";
/*tout est ok*/
    
    if (mail($destinataire,$subject,$body,$headers)){
    /*petite secu*/
    $message=NULL;
    $mail=NULL;
    $nom=NULL;
    $objet=NULL;
    $_POST=NULL;
    $_SESSION['verif']=NULL; /*anti double post*/
    $destinataire=NULL;
    echo '<h3 align="center">MESSAGGIO INVIATO CON SUCCESO, GRAZIE ! - <a href="javascript:history.back();">TORNA AL FORMULARIO</a><br /></h3>';exit; 
    /* ou redirection header('Location: http://unsite.fr/merci.html');exit; ... */
    }else{
    /*petite secu*/
    $message=NULL;
    $mail=NULL;
    $nom=NULL;
    $objet=NULL;
    $_POST=NULL;
    $_SESSION['verif']=NULL;  /*anti double post*/
    $destinataire=NULL;
    echo '<h3 align="center">Spiacente messaggio non inviato ! - <a href="javascript:history.back();">TORNA AL FORMULARIO</a><br /></h3>';exit;
    /* ou redirection header('Location: http://unsite.fr/erreur.html');exit; ... */
    }

/*petite secu*/
$message=NULL;
$mail=NULL;
$nom=NULL;
$objet=NULL;
$_POST=NULL;
$destinataire=NULL;

} else {
echo $error='<h3 align="center">ERREUR SUR LE CODE DE SECURITE - <a href="javascript:history.back();">Retour au formulaire</a></h3>';exit;
}

?>
