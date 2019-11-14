import express from 'express';
import bodyParser from 'body-parser';
import path from 'path';
import cookieParser from 'cookie-parser';

const app = express(), DIST_DIR = __dirname, HTML_FILE = path.join(DIST_DIR, 'index.html')

// Redirect to https
app.get('*', function(req,res,next) {
    if(req.headers['x-forwarded-proto'] != 'https')
      res.redirect('https://'+req.hostname+req.url)
    else
      next() /* Continue to other routes if we're not redirecting */
  });
app.use(cookieParser());
app.use(express.static(DIST_DIR))
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json())
app.use(function (req, res, next) {
    res.header('Content-Type', 'application/json');
    next();
  });

app.get('/', (request, response) => {
    response.sendFile(HTML_FILE)
})

const PORT = process.env.PORT || 8080

app.listen(PORT, () => {
    console.log(`TFS-Monitoring listening on ${PORT}. Press Ctrl+C to quit.`)
})