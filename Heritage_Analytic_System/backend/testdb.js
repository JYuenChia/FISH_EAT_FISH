require("dotenv").config(); // 👈 ADD THIS LINE
const pool = require("./db");

(async () => {
  const [rows] = await pool.query("SELECT 1");
  console.log("MySQL connected");
})();
