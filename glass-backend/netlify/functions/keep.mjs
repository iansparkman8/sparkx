import { getStore } from "@netlify/blobs";

function refused(text) {
  return /hurt|kill|bomb|hate/i.test(text || "");
}

export default async (req) => {
  if (req.method === "GET") {
    const rows = (await getStore("shelf").get("rows", { type: "json" })) || [];
    return Response.json({ items: rows.slice(0, 20) });
  }
  if (req.method !== "POST") return new Response("method", { status: 405 });
  const body = await req.json().catch(() => ({}));
  const question = String(body.question || "").slice(0, 500);
  if (!question) return Response.json({ error: "empty" }, { status: 400 });
  const store = getStore("shelf");
  const rows = (await store.get("rows", { type: "json" })) || [];
  if (refused(question)) {
    rows.unshift({ title: "Refusal", kind: "refusal", body: "A harmful ask was set down. The text was not kept.", route: "refusal", at: new Date().toISOString() });
    await store.setJSON("rows", rows.slice(0, 100));
    return Response.json({ route: "refusal", kept: false });
  }
  rows.unshift({ title: "Kept question", kind: "question", body: question, route: "keep", at: new Date().toISOString() });
  await store.setJSON("rows", rows.slice(0, 100));
  return Response.json({ route: "keep", kept: true });
};

export const config = { path: "/api/keep" };
