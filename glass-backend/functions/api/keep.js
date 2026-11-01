function refused(text) {
  return /hurt|kill|bomb|hate/i.test(text || "");
}

export async function onRequestPost(context) {
  const body = await context.request.json().catch(() => ({}));
  const question = String(body.question || "").slice(0, 500);
  if (!question) return Response.json({ error: "empty" }, { status: 400 });
  if (!context.env.SHELF) return Response.json({ route: "no-database", kept: false }, { status: 503 });
  if (refused(question)) {
    await context.env.SHELF.prepare(
      "INSERT INTO shelf (title, kind, body, route) VALUES (?, ?, ?, ?)"
    ).bind("Refusal", "refusal", "A harmful ask was set down. The text was not kept.", "refusal").run();
    return Response.json({ route: "refusal", kept: false });
  }
  const saved = await context.env.SHELF.prepare(
    "INSERT INTO shelf (title, kind, body, route) VALUES (?, ?, ?, ?)"
  ).bind("Kept question", "question", question, "keep").run();
  return Response.json({ route: "keep", kept: true, id: saved.meta.last_row_id });
}
