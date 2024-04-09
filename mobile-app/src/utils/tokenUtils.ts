let apiToken = '';

export function setExpoToken(token: string) {
  apiToken = token;
}

export function getExpoToken() {
  return apiToken;
}