import { ok, badRequest, serverError } from 'wix-http-functions';
import wixData from 'wix-data';

function refused(text) {
  return /hurt|kill|bomb|hate/i.test(text || '');
}

export async function post_keep(request) {
  try {
    const body = await request.body.json();
    const question = String(body.question || '').slice(0, 500);
    if (!question) return badRequest({ body: { error: 'empty' } });
    if (refused(question)) {
      await wixData.insert('ara-shelf', {
        title: 'Refusal',
        kind: 'refusal',
        body: 'A harmful ask was set down. The text was not kept.',
        spell: 'backend',
        choice: 'refused'
      });
      return ok({ body: { route: 'refusal', kept: false } });
    }
    const item = await wixData.insert('ara-shelf', {
      title: 'Kept question',
      kind: 'question',
      body: question,
      spell: 'backend',
      choice: 'kept'
    });
    return ok({ body: { route: 'keep', id: item._id, kept: true } });
  } catch (error) {
    return serverError({ body: { error: String(error) } });
  }
}

export async function get_shelf(request) {
  try {
    const rows = await wixData.query('ara-shelf').descending('_createdDate').limit(20).find();
    return ok({ body: { items: rows.items.map(row => ({ title: row.title, kind: row.kind, body: row.body })) } });
  } catch (error) {
    return serverError({ body: { error: String(error) } });
  }
}
