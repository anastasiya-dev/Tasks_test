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
      <title>Hello, world!</title>
   </head>
   <body>
      <nav class="navbar navbar-expand-lg navbar-light bg-light">
         <a class="navbar-brand" href="#">Navbar</a>
         <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
         <span class="navbar-toggler-icon"></span>
         </button>
         <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
               <li class="nav-item active">
                  <a class="nav-link" href="/blockchain-web/home.html">Home <span class="sr-only">(current)</span></a>
               </li>
               <li class="nav-item">
                  <a class="nav-link" href="#">Link</a>
               </li>
               <li class="nav-item dropdown">
                  <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                  Recipients
                  </a>
                  <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                     <a class="dropdown-item" href="/hello-spring/new-recipient.html">New Recipient</a>
                     <a class="dropdown-item" href="/hello-spring/recipient-list.html">Edit Recipient</a>
                     <div class="dropdown-divider"></div>
                     <a class="dropdown-item" href="#">Delete Recipient</a>
                  </div>
               </li>
               <li class="nav-item">
                  <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>
               </li>
            </ul>
            <form class="form-inline my-2 my-lg-0" action="/hello-spring/search.html">
               <input class="form-control mr-sm-2" type="search" name="search" placeholder="Search" aria-label="Search">
               <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>
         </div>
      </nav>