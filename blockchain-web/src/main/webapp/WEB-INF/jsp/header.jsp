<!doctype html>
<html lang="en">
   <head>
      <style>
         * {
         box-sizing: border-box;
         }
         *:before,
         *:after {
         box-sizing: border-box;
         }
         html,
         body {
         height: 100%;
         position: relative;
         }
         .main-container {
         min-height: 100vh; /* will cover the 100% of viewport */
         overflow: hidden;
         display: block;
         position: relative;
         padding-bottom: 100px; /* height of your footer */
         }
         footer {
         position: relative;
         bottom: 0;
         width: 100%;
         }
      </style>
      <!-- Required meta tags -->
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
      <!-- Bootstrap CSS -->
      <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z" crossorigin="anonymous">
      <title>Blockchain</title>
   </head>
   <body>
      <nav class="navbar navbar-expand-lg navbar-light bg-light">
         <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
               <li class="nav-item active">
                  <a class="nav-link" href="/blockchain-web/home">Home <span class="sr-only">(current)</span></a>
               </li>
               <li class="nav-item">
                  <a class="nav-link" href="/blockchain-web/${userId}/wallet/${walletId}">Back to wallet</a>
               </li>
               <li class="nav-item dropdown">
                  <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                  Personal Info
                  </a>
                  <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                     <a class="dropdown-item" href="/blockchain-web/${userId}/edit">Edit</a>
                     <a class="dropdown-item" href="/blockchain-web/${userId}/delete">Delete</a>
                  </div>
               </li>
               <li class="nav-item dropdown">
                  <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                  Wallets
                  </a>
                  <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                     <a class="dropdown-item" href="/blockchain-web/${userId}/create-wallet">Create</a>
                     <a class="dropdown-item" href="/blockchain-web/${userId}/wallet-all">View all</a>
                  </div>
               </li>
            </ul>
         </div>
      </nav>