const express = require('express');
const app = express();
const morgan = require('morgan');
const bodyParser = require('body-parser');

//settings
app.set('port', 3100);

//middlewares
app.use(morgan('dev'));
app.use(bodyParser.json());

//routes
require('./routes/userRoutes')(app);


//statis files


app.listen(app.get('port'), () => {
		console.log('server on port 3100');
	});
