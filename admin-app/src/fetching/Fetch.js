/*
    Koristiti ovu funkciju za fetchanje u buducnosti kad god je to moguce.
 */
export async function fetchData(url, method, body) {
    const headers = new Headers();

    const token = localStorage.getItem('token');
    if (token) {
        headers.append('Authorization', `Bearer ${ token }`);
    }

    headers.append('Content-Type', 'application/json');

    const res = await fetch(url, {
        method: method || 'GET',
        headers: headers,
        body: body ? JSON.stringify(body) : null
    });

    if (!res) {
        return { success: false };
    }

    const data = res.ok && res.body ? await res.json() : null;

    if (res.ok) {
        //na svaki ispravan rezultat treba da dobijemo novi token da refreshamo stari
        const newToken = res.headers.get('Auth-Token');
        if (newToken) {
            localStorage.setItem('token', newToken);
        }
    }

    return { data: data, success: res.ok };
}