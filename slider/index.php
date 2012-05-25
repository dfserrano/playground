<!doctype html>
<html>
    <head>
        <meta charset=utf-8>
        <title>Slider</title>
        
        <!-- Estilos -->
        <link rel="stylesheet" href="css/style.css">
        
        <!-- Scripts -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.js"></script>
    </head>
    <body>
        <div id="container" class="container">
            <div id="window" class="window">
                <div id="reel" class="reel">
                    <div class="piece">
                        <img src="img/1.jpg" alt="1" />
                    </div>
                    <div class="piece">
                        <img src="img/2.jpg" alt="1" />
                    </div>
                    <div class="piece">
                        <img src="img/3.jpg" alt="1" />
                    </div>
                    <div class="piece">
                        <img src="img/4.jpg" alt="1" />
                    </div>
                    <div class="piece">
                        <img src="img/5.jpg" alt="1" />
                    </div>
                </div>
            </div>
            
            <div id="pages" class="pages">
                
            </div>
        </div>
        
        <br/><br/>
        
        <div id="container2" class="container">
            <div id="window2" class="window">
                <div id="reel2" class="reel">
                    <div class="piece">
                        <img src="img/1.jpg" alt="1" />
                    </div>
                    <div class="piece">
                        <img src="img/2.jpg" alt="1" />
                    </div>
                    <div class="piece">
                        <img src="img/3.jpg" alt="1" />
                    </div>
                    <div class="piece">
                        <img src="img/4.jpg" alt="1" />
                    </div>
                    <div class="piece">
                        <img src="img/5.jpg" alt="1" />
                    </div>
                </div>
            </div>
            
            <div id="pages2" class="pages">
                
            </div>
        </div>
        
        <script type="text/javascript" src="js/slider.js"></script>
        <script type="text/javascript">
            $(window).load(function() {
                var sld = new Slider('#container', {interval: 2000, numbers: true});
                sld.start();

                var sld2 = new Slider('#container2', {interval: 1000, numbers: false});
                sld2.start();
            });
            
    </script>
    </body>
</html>
