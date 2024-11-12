// Do not user localhost for loopback address
// as it will break on production server
export const BASE_URL = process.env.REACT_APP_BASE_URL;
