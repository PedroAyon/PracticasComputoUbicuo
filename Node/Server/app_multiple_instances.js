const express = require('express');
const morgan = require('morgan');
const bodyParser = require('body-parser');

const startPort = 3000;
const instances = 22;

for (let i = 0; i < instances; i++) {
  const app = express();
  const port = startPort + i;

  // Apply middlewares
  app.use(morgan('dev'));
  app.use(bodyParser.json());

  // Set the initial equipo value for this instance (e.g., using instance number)
  const initialEquipo = i + 1; // or any dynamic logic

  // Pass the instance-specific equipo to the routes module
  require('./routes/userRoutes')(app, initialEquipo);

  // Start the server on a unique port
  app.listen(port, () => {
    console.log(`Server instance ${i} running on port ${port}`);
  });
}

