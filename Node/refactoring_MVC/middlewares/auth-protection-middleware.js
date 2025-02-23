function guardRoute(req, res, next) {
  if (!req.locals.isAuth) {
    return res.redirect('/401');
  }

  next();
}

module.exports = guardRoute;
