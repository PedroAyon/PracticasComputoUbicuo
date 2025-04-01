const express = require('express');
const morgan = require('morgan');
const bodyParser = require('body-parser');

const app = express();
const port = 3100

app.use(morgan('dev'));
app.use(bodyParser.json());

require('./routes/userRoutes')(app, 1);
app.listen(port, () => {
  console.log(`Server instance running on port ${port}`);
});

