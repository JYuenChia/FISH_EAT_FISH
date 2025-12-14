app.post("/api/analytics/event", async (req, res) => {
  const { event_type, metadata, user_id } = req.body;

  await db.query(
    "INSERT INTO analytics_events (event_type, metadata, user_id) VALUES ($1, $2, $3)",
    [event_type, metadata, user_id || "anonymous"]
  );

  res.json({ success: true });
});
