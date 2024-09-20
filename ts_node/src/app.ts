import express, { Request, Response, NextFunction } from 'express'; // Import express on TS file, because es6 module has a import and export keyword, cf) node require and module.exports
import { json } from 'body-parser';
import todoRoutes from './routes/todo'; // Import todoRoutes from routes/todo.ts

const app = express();

app.use(express.json());

app.use('/todos', todoRoutes);

app.use((err: Error, req: Request, res: Response, next: NextFunction) => {
  res.status(500).json({ message: err.message });
});

app.listen(3000);
