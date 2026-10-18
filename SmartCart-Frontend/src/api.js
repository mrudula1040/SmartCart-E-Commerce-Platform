const BASE=''
export const session={
  get(){try{return JSON.parse(sessionStorage.getItem('smartcartUser')||'null')}catch{return null}},
  set(v){sessionStorage.setItem('smartcartUser',JSON.stringify(v))},
  clear(){sessionStorage.removeItem('smartcartUser')}
}
export async function api(path, options={}){
  const user=session.get()
  const headers={'Content-Type':'application/json',...(options.headers||{})}
  if(user?.token) headers.Authorization=`Bearer ${user.token}`
  const res=await fetch(BASE+path,{...options,headers})
  const text=await res.text()
  let data=null; try{data=text?JSON.parse(text):null}catch{data=text}
  if(res.status===401 && user?.token && !path.startsWith('/api/auth')){
    session.clear()
    window.location.reload()
    throw new Error('Session expired. Please login again.')
  } if(!res.ok) throw new Error(data?.message|| data?.console.error||data||`Request failed(${res.status})`)
    return data

}
export const get=(p)=>api(p)
export const post=(p,b)=>api(p,{method:'POST',body:JSON.stringify(b)})
export const put=(p,b)=>api(p,{method:'PUT',body:JSON.stringify(b)})
export const del=(p)=>api(p,{method:'DELETE'})
