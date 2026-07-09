const API_BASE = import.meta.env.VITE_API_BASE_URL || "http://localhost:8085";


function getAuthHeaders() {

  const token = localStorage.getItem("token");

  return token
    ? {
        Authorization: `Bearer ${token}`
      }
    : {};

}



async function handle(res) {

  if (!res.ok) {

    let message = `Request failed (${res.status})`;

    try {

      const body = await res.json();

      message =
        body.error ||
        body.message ||
        Object.values(body)[0] ||
        message;

    } catch (_) {

    }

    throw new Error(message);
  }


  if (res.status === 204)
    return null;


  return res.json();

}




// PUBLIC API

export function fetchJobs(filters = {}) {


  const params = new URLSearchParams();


  Object.entries(filters).forEach(([key,value])=>{

    if(value)
      params.set(key,value);

  });


  const qs = params.toString();


  return fetch(
    `${API_BASE}/api/jobs${qs ? `?${qs}` : ""}`,
    {
      headers:{
        ...getAuthHeaders()
      }
    }
  )
  .then(handle);

}




export function fetchJob(id) {


  return fetch(
    `${API_BASE}/api/jobs/${id}`,
    {
      headers:{
        ...getAuthHeaders()
      }
    }
  )
  .then(handle);

}





// EMPLOYER CREATE JOB

export function createJob(payload) {


  return fetch(
    `${API_BASE}/api/jobs`,
    {
      method:"POST",

      headers:{
        "Content-Type":"application/json",
        ...getAuthHeaders()
      },

      body:JSON.stringify(payload)

    }
  )
  .then(handle);

}






// CANDIDATE APPLY JOB

export function applyToJob(jobId,payload){


  return fetch(
    `${API_BASE}/api/jobs/${jobId}/applications`,
    {

      method:"POST",

      headers:{
        "Content-Type":"application/json",
        ...getAuthHeaders()
      },


      body:JSON.stringify(payload)

    }
  )
  .then(handle);

}