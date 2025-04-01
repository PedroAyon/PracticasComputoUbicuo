module.exports = function(app, equipo) {
  let equipoInstancia = equipo;

  app.get('/metoca/', (req, res) => {
    if (equipoInstancia == req.query.id) {
      res.json({ res: 'SI' });
      equipoInstancia = null;
    } else {
      res.json({ res: 'NO' });
    }
  });

  app.get('/tetoca/', (req, res) => {
    equipoInstancia = equipo;
    while (equipoInstancia === equipo) {
      equipoInstancia = Math.floor(Math.random() * 20) + 1;
    }
    console.log(equipoInstancia);
    res.send('ENTENDIDO');
  });
};

