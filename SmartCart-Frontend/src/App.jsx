import React,{useEffect,useMemo,useState} from 'react'
import {api,session,get,post,put,del} from './api'

const money=n=>`₹${Number(n||0).toLocaleString('en-IN')}`

export default function App(){
 const [user,setUser]=useState(session.get())

const [page,setPage]=useState(
  user ? (user.role === 'ADMIN' ? 'admin' : 'home') : 'login'
)
 const [products,setProducts]=useState([]),[categories,setCategories]=useState([])
 const [cart,setCart]=useState([]),[wishlist,setWishlist]=useState([]),[orders,setOrders]=useState([])
 const [search,setSearch]=useState(''),[cat,setCat]=useState('')
 const [notice,setNotice]=useState(''),[loading,setLoading]=useState(false)
 const [editing,setEditing]=useState(null)

 const loadBase=async()=>{
   try{setProducts(await get('/api/products'));}catch(e){setNotice(e.message)}
   try{setCategories(await get('/api/categories'));}catch(e){}
 }
 const loadUserData = async () => {
  if (!user) return

  try {
    setCart(await get(`/api/cart/user/${user.userId}`))
  } catch {}

  try {
    setWishlist(await get(`/api/wishlist/user/${user.userId}`))
  } catch {}

  try {
    if (user.role === 'ADMIN') {
      setOrders(await get('/api/orders'))
    } else {
      setOrders(await get(`/api/orders/user/${user.userId}`))
    }
  } catch {}
}
 useEffect(()=>{loadBase()},[])
 useEffect(()=>{loadUserData()},[user])
 const logout=()=>{session.clear();setUser(null);setPage('login');setCart([]);setWishlist([])}
 const filtered=useMemo(()=>products.filter(p=>
   (!search||p.productName?.toLowerCase().includes(search.toLowerCase())) && (!cat||String(p.categoryId)===String(cat))
 ),[products,search,cat])
 const cartRows=cart.map(c=>({...c,product:products.find(p=>p.id===c.productId)})).filter(x=>x.product)
 const total=cartRows.reduce((s,x)=>s+Number(x.product.price||0)*x.quantity,0)
 const wishIds=new Set(wishlist.map(w=>w.productId))

 async function addCart(p){if(!user)return setPage('login');try{const old=cart.find(c=>c.productId===p.id);let r;if(old) r=await put(`/api/cart/${old.id}`,{userId:user.userId,productId:p.id,quantity:old.quantity+1}); else r=await post('/api/cart',{userId:user.userId,productId:p.id,quantity:1});setCart(old?cart.map(c=>c.id===r.id?r:c):[...cart,r]);setNotice('Added to cart')}catch(e){setNotice(e.message)}}
 async function toggleWish(p){if(!user)return setPage('login');try{const old=wishlist.find(w=>w.productId===p.id);if(old){await del(`/api/wishlist/${old.id}`);setWishlist(wishlist.filter(w=>w.id!==old.id));}else{const r=await post('/api/wishlist',{userId:user.userId,productId:p.id});setWishlist([...wishlist,r]);}}catch(e){setNotice(e.message)}}
 async function changeQty(c,q){if(q<1)return removeCart(c.id);try{const r=await put(`/api/cart/${c.id}`,{userId:user.userId,productId:c.productId,quantity:q});setCart(cart.map(x=>x.id===r.id?r:x))}catch(e){setNotice(e.message)}}
 async function removeCart(id){try{await del(`/api/cart/${id}`);setCart(cart.filter(x=>x.id!==id))}catch(e){setNotice(e.message)}}
 async function checkout(method){if(!user||!cartRows.length)return;setLoading(true);try{const order=await post('/api/orders',{userId:user.userId,totalAmount:total, status:'PLACED'});await post('/api/payments',{amount:total,orderId:order.id,paymentStatus:'PAID',paymentMethod:method});for(const c of cart) await del(`/api/cart/${c.id}`);setCart([]);await loadUserData();setNotice('Order placed successfully');setPage('orders')}catch(e){setNotice(e.message)}finally{setLoading(false)}}

if(page==='login'||page==='register') return <Auth mode={page} onSuccess={u=>{session.set(u);setUser(u);setPage(u.role==='ADMIN'?'admin':'home')}} onSwitch={()=>setPage(page==='login'?'register':'login')}/>
 return <div className="app">
 {page !== 'admin' && (
<header><div className="brand" onClick={()=>setPage('home')}>🛒 SmartCart</div><nav>
   <button onClick={()=>setPage('home')}>Home</button><button onClick={()=>setPage('products')}>Products</button><button onClick={()=>setPage('wishlist')}>♡ Wishlist ({wishlist.length})</button><button onClick={()=>setPage('cart')}>🛒 Cart ({cart.length})</button><button onClick={()=>setPage('orders')}>Orders</button>{user.role==='ADMIN'&&<button onClick={()=>setPage('admin')}>Admin</button>}
   <span className="user">Hi, {user.name}</span><button className="outline" onClick={logout}>Logout</button>
  </nav></header>)}
  {notice&&<div className="notice">{notice}<button onClick={()=>setNotice('')}>×</button></div>}
  {page==='home'&&<Home go={()=>setPage('products')}/>} 
  {page==='products'&&<Products products={filtered} categories={categories} search={search} setSearch={setSearch} cat={cat} setCat={setCat} addCart={addCart} toggleWish={toggleWish} wishIds={wishIds}/>} 
  {page==='wishlist'&&<Wishlist products={products.filter(p=>wishIds.has(p.id))} toggleWish={toggleWish} addCart={addCart}/>} 
  {page==='cart'&&<Cart rows={cartRows} total={total} changeQty={changeQty} removeCart={removeCart} checkout={checkout} loading={loading}/>} 
  {page==='orders'&&<Orders orders={orders}/>} 
   {page==='admin'&&
  <Admin
    products={products}
    setProducts={setProducts}
    categories={categories}
    setCategories={setCategories}
    orders={orders}
    setOrders={setOrders}
    editing={editing}
    setEditing={setEditing}
    refreshProducts={loadBase}
    onLogout={logout}
  />
}
 </div>
}

function Auth({mode,onSuccess,onSwitch}){const [form,setForm]=useState({name:'',email:'',password:''}),[err,setErr]=useState(''),[busy,setBusy]=useState(false);async function submit(e){e.preventDefault();setBusy(true);setErr('');try{const body=mode==='login'?{email:form.email,password:form.password}:{name:form.name,email:form.email,password:form.password,role:'CUSTOMER'};const r=await post(`/api/auth/${mode==='login'?'login':'register'}`,body);onSuccess(r)}catch(e){setErr(e.message)}finally{setBusy(false)}}return <div className="auth"><div className="authCard"><div className="logo">🛒</div><h1>SmartCart</h1><p>{mode==='login'?'Welcome back':'Create your account'}</p>{err&&<div className="error">{err}</div>}<form onSubmit={submit}>{mode==='register'&&<input placeholder="Full name" required value={form.name} onChange={e=>setForm({...form,name:e.target.value})}/>}<input type="email" placeholder="Email" required value={form.email} onChange={e=>setForm({...form,email:e.target.value})}/><input type="password" placeholder="Password" required value={form.password} onChange={e=>setForm({...form,password:e.target.value})}/><button className="primary wide" disabled={busy}>{busy?'Please wait...':mode==='login'?'Login':'Register'}</button></form><button className="link" onClick={onSwitch}>{mode==='login'?"Don't have an account? Register":"Already have an account? Login"}</button></div></div>}

function Home({go}){return <main className="hero"><div><span className="pill">Simple • Fast • Smart Shopping</span><h2>Everything you need,<br/><b>in one cart.</b></h2><p>Browse products, save favourites, add to cart and place orders easily.</p><button className="primary" onClick={go}>Shop Products →</button></div><div className="heroArt">🛍️</div></main>}
function Products({products,categories,search,setSearch,cat,setCat,addCart,toggleWish,wishIds}){return <main className="content"><div className="titleRow"><div><h2>Products</h2><p>Find something you like.</p></div><div className="filters"><input placeholder="Search products..." value={search} onChange={e=>setSearch(e.target.value)}/><select value={cat} onChange={e=>setCat(e.target.value)}><option value="">All categories</option>{categories.map(c=><option key={c.id} value={c.id}>{c.categoryName}</option>)}</select></div></div><div className="grid">{products.map(p=><ProductCard key={p.id} p={p} addCart={addCart} toggleWish={toggleWish} wished={wishIds.has(p.id)}/>)}{!products.length&&<div className="empty">No products found.</div>}</div></main>}

function ProductCard({p,addCart,toggleWish,wished}){
  const [reviews,setReviews]=useState([])
  const [showReview,setShowReview]=useState(false)
  const [rating,setRating]=useState(5)
  const [comment,setComment]=useState('')
  const [reviewMsg,setReviewMsg]=useState('')

  async function loadReviews(){
    try{
      const r=await get(`/api/reviews/product/${p.id}`)
      setReviews(r||[])
      setShowReview(true)
    }catch(e){
      setReviewMsg(e.message)
    }
  }
async function submitReview(){
  const user=session.get()

  if(!user){
    setReviewMsg('Please login to write a review')
    return
  }

  if(!comment.trim()){
    setReviewMsg('Please enter a comment')
    return
  }

  try{
    const r=await post('/api/reviews',{
      user:{
        id:user.userId
      },
      product:{
        id:p.id
      },
      rating:Number(rating),
      comment:comment.trim()
    })

    setReviews([...reviews,r])
    setComment('')
    setRating(5)
    setReviewMsg('Review added successfully')

  }catch(e){
    console.log('Review Error:',e)
    setReviewMsg(e.message || 'Failed to add review')
  }
}

  return (
    <article className="card">

      <div className="pic">
        {p.imgURL
          ? <img
              src={p.imgURL}
              onError={e=>e.currentTarget.style.display='none'}
              alt={p.productName}
            />
          : <span>🛍️</span>
        }

        <button
          className="heart"
          onClick={()=>toggleWish(p)}
        >
          {wished ? '♥' : '♡'}
        </button>
      </div>

      <div className="cardBody">

        <small>{p.categoryName || 'Product'}</small>

        <h3>{p.productName}</h3>

        <p>{p.description}</p>

        <div className="cardBottom">
          <b>{money(p.price)}</b>

          <span>
            {p.stock > 0
              ? `${p.stock} in stock`
              : 'Out of stock'}
          </span>
        </div>

        <button
          className="primary wide"
          disabled={!p.stock}
          onClick={()=>addCart(p)}
        >
          Add to Cart
        </button>

        <button
          className="reviewButton wide"
          onClick={loadReviews}
        >
          ⭐ Reviews ({reviews.length})
        </button>

        {showReview && (
          <div className="reviewBox">

            <h4>Customer Reviews</h4>

            {!reviews.length && (
              <p className="noReviews">
                No reviews yet. Be the first!
              </p>
            )}

            {reviews.map(r => (
              <div className="reviewItem" key={r.id}>

                <div className="stars">
                  {'★'.repeat(Number(r.rating || 0))}
                  {'☆'.repeat(5 - Number(r.rating || 0))}
                </div>

                <p>{r.comment}</p>

              </div>
            ))}

            <div className="writeReview">

              <h4>Write a Review</h4>

              <select
                value={rating}
                onChange={e=>setRating(Number(e.target.value))}
              >
                <option value={5}>★★★★★ - Excellent</option>
                <option value={4}>★★★★☆ - Very Good</option>
                <option value={3}>★★★☆☆ - Good</option>
                <option value={2}>★★☆☆☆ - Average</option>
                <option value={1}>★☆☆☆☆ - Poor</option>
              </select>

             <textarea
  placeholder="Write your review..."
  value={comment}
  onChange={e => setComment(e.target.value)}
/>

<button
  type="button"
  className="primary wide"
  
  onClick={submitReview}
>
  Submit Review
</button>

              {reviewMsg && (
                <small className="reviewMsg">
                  {reviewMsg}
                </small>
              )}

            </div>

          </div>
        )}

      </div>
    </article>
  )
}

function Wishlist({products,toggleWish,addCart}){return <main className="content"><h2>Wishlist</h2><div className="grid">{products.map(p=><ProductCard key={p.id} p={p} addCart={addCart} toggleWish={toggleWish} wished/>)}{!products.length&&<div className="empty">Your wishlist is empty.</div>}</div></main>}

function Cart({rows,total,changeQty,removeCart,checkout,loading}){const [method,setMethod]=useState('COD');return <main className="content"><h2>Your Cart</h2>{!rows.length?<div className="empty">Your cart is empty.</div>:<div className="cartLayout"><div>{rows.map(x=><div className="cartRow" key={x.id}><div className="mini">🛍️</div><div className="grow"><h3>{x.product.productName}</h3><span>{money(x.product.price)}</span></div><div className="qty"><button onClick={()=>changeQty(x,x.quantity-1)}>−</button><b>{x.quantity}</b><button onClick={()=>changeQty(x,x.quantity+1)}>+</button></div><b>{money(x.product.price*x.quantity)}</b><button className="dangerText" onClick={()=>removeCart(x.id)}>Remove</button></div>)}</div><aside className="summary"><h3>Order Summary</h3><div><span>Subtotal</span><b>{money(total)}</b></div><div><span>Delivery</span><b>Free</b></div><hr/><div className="big"><span>Total</span><b>{money(total)}</b></div><select value={method} onChange={e=>setMethod(e.target.value)}><option value="COD">Cash on Delivery</option><option value="CARD">Card</option><option value="UPI">UPI</option></select><button className="primary wide" disabled={loading} onClick={()=>checkout(method)}>{loading?'Placing...':'Place Order'}</button></aside></div>}</main>}
function Orders({orders}){return <main className="content"><h2>My Orders</h2>{!orders.length?<div className="empty">No orders yet.</div>:<div className="orders">{orders.map(o=><div className="order" key={o.id}><div><b>Order #{o.id}</b><p>{o.orderDate?new Date(o.orderDate).toLocaleString():'-'}</p></div><strong>{money(o.totalAmount)}</strong><span className="status">{o.status}</span></div>)}</div>}</main>}

function Admin({ products, setProducts, categories, setCategories, orders, setOrders, editing, setEditing, refreshProducts, onLogout }) {
  const [tab, setTab] = useState('dashboard')
  const [msg, setMsg] = useState('')
  const [customers, setCustomers] = useState([])

  const [form, setForm] = useState({
    productName: '',
    description: '',
    price: '',
    stock: '',
    imgURL: '',
    categoryId: ''
  })

  const [catForm, setCatForm] = useState({
    categoryName: '',
    description: ''
  })

  // Inventory
  const [inventory, setInventory] = useState([])
  const [inventoryEditing, setInventoryEditing] = useState(null)

  const [inventoryForm, setInventoryForm] = useState({
    productId: '',
    quantity: '',
    warehouseLocation: ''
  })

  // ---------------- PRODUCT ----------------

  async function saveProduct(e) {
    e.preventDefault()

    try {
      const body = {
        ...form,
        price: Number(form.price),
        stock: Number(form.stock),
        categoryId: Number(form.categoryId)
      }

      const r = editing
        ? await put(`/api/products/${editing}`, body)
        : await post('/api/products', body)

      setProducts(
        editing
          ? products.map(p => p.id === r.id ? r : p)
          : [...products, r]
      )

      setEditing(null)

      setForm({
        productName: '',
        description: '',
        price: '',
        stock: '',
        imgURL: '',
        categoryId: ''
      })

      setMsg('Product saved successfully')
    } catch (e) {
      setMsg(e.message)
    }
  }

  async function removeProduct(id) {
    if (!confirm('Delete product?')) return

    try {
      await del(`/api/products/${id}`)
      setProducts(products.filter(p => p.id !== id))
      setMsg('Product deleted')
    } catch (e) {
      setMsg(e.message)
    }
  }

  // ---------------- CATEGORY ----------------

  async function saveCat(e) {
    e.preventDefault()

    try {
      const r = await post('/api/categories', catForm)

      setCategories([...categories, r])

      setCatForm({
        categoryName: '',
        description: ''
      })

      setMsg('Category added successfully')
    } catch (e) {
      setMsg(e.message)
    }
  }

  // ---------------- INVENTORY ----------------

  async function loadInventory() {
    try {
      const r = await get('/api/inventory')
      setInventory(r || [])
    } catch (e) {
      setMsg(e.message)
    }
  }

  useEffect(() => {
  if (tab!=='inventory') return
    loadInventory()
    const timer = setInterval(()=>{
      loadInventory()
    },2000)
    return ()=> clearInterval(timer)
  
}, [tab])
 useEffect(() => {
  if (tab === 'orders' || tab === 'dashboard') {
    loadAdminOrders()
  }
}, [tab])
async function loadAdminCustomers() {
  try {
    const result = await get('/api/users')
    setCustomers(Array.isArray(result) ? result : [])
  } catch (e) {
    setMsg('Customer analytics could not be loaded: ' + e.message)
  }
}

useEffect(() => {
  if (tab === 'dashboard') {
    loadAdminCustomers()
  }
}, [tab])
async function loadAdminOrders() {
  try {
    const r = await get('/api/orders')
    setOrders(r || [])
  } catch (e) {
    setMsg(e.message)
  }
}

  function getProductName(productId) {
    const product = products.find(
      p => Number(p.id) === Number(productId)
    )

    return product
      ? product.productName
      : `Product #${productId}`
  }

  function resetInventoryForm() {
    setInventoryEditing(null)

    setInventoryForm({
      productId: '',
      quantity: '',
      warehouseLocation: ''
    })
  }

  async function saveInventory(e) {
    e.preventDefault()

    try {
      const body = {
        productId: Number(inventoryForm.productId),
        quantity: Number(inventoryForm.quantity),
        warehouseLocation: inventoryForm.warehouseLocation
      }

      const r = inventoryEditing
        ? await put(`/api/inventory/${inventoryEditing}`, body)
        : await post('/api/inventory', body)

            if (inventoryEditing) {
        setInventory(
          inventory.map(item =>
            item.id === r.id ? r : item
          )
        )
      } else {
        setInventory([...inventory, r])
      }

      
await refreshProducts()
      resetInventoryForm()

      setMsg(
        inventoryEditing
          ? 'Inventory updated successfully'
          : 'Inventory added successfully'
      )
    } catch (e) {
      setMsg(e.message)
    }
  }

  function editInventory(item) {
    setInventoryEditing(item.id)

    setInventoryForm({
      productId: item.productId,
      quantity: item.quantity,
      warehouseLocation: item.warehouseLocation || ''
    })

    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    })
  }

  async function removeInventory(id) {
    if (!confirm('Delete inventory record?')) return

    try {
      await del(`/api/inventory/${id}`)

      setInventory(
        inventory.filter(item => item.id !== id)
      )

      setMsg('Inventory deleted')
    } catch (e) {
      setMsg(e.message)
    }
  }

 return (
    <main className="content">
      <div className="adminHeader">
  <div>
    <h1>Admin Dashboard</h1>
    <p>Welcome back, Admin! Manage your SmartCart store.</p>
  </div>
  <span className="adminBadge">🛡️ Administrator</span>
</div>

      {/* ADMIN TABS */}

      <div className="adminTabs">

        <button
  className={tab === 'dashboard' ? 'active' : ''}
  onClick={() => setTab('dashboard')}
>
  📊 Dashboard
</button>
<button
  className={tab === 'products' ? 'active' : ''}
  onClick={() => setTab('products')}
>
  📦 Products
</button>
        <button
          className={tab === 'categories' ? 'active' : ''}
          onClick={() => setTab('categories')}
        >
          Categories
        </button>

        <button
          className={tab === 'inventory' ? 'active' : ''}
          onClick={() => setTab('inventory')}
        >
          Inventory
        </button>

        <button
          className={tab === 'orders' ? 'active' : ''}
          onClick={() => setTab('orders')}
        >
          Orders
        </button>
        <button
  className="outline"
  onClick={onLogout}
>
  Logout
</button>

      </div>

      {msg && (
        <div className="notice">
          {msg}
          <button onClick={() => setMsg('')}>×</button>
        </div>
      )}

{/* ================= DASHBOARD ANALYTICS ================= */}

{tab === 'dashboard' && (() => {
  const customerCount = customers.filter(
    u => String(u.role || '').toUpperCase() !== 'ADMIN'
  ).length

  const totalOrderValue = orders.reduce(
    (sum, o) => sum + Number(o.totalAmount || 0),
    0
  )

  const lowStockItems = inventory.filter(
    item => Number(item.quantity || 0) <= 5
  )

  const recentOrders = [...orders]
    .sort((a, b) =>
      new Date(b.orderDate || 0) - new Date(a.orderDate || 0)
    )
    .slice(0, 5)

  const monthlySales = Array.from({ length: 6 }, (_, i) => {
    const date = new Date()
    date.setDate(1)
    date.setMonth(date.getMonth() - (5 - i))

    const value = orders
      .filter(o => {
        if (!o.orderDate) return false
        const d = new Date(o.orderDate)
        return d.getFullYear() === date.getFullYear()
          && d.getMonth() === date.getMonth()
      })
      .reduce((sum, o) => sum + Number(o.totalAmount || 0), 0)

    return {
      label: date.toLocaleString('en-IN', { month: 'short' }),
      value
    }
  })

  const maxSales = Math.max(
    1,
    ...monthlySales.map(m => m.value)
  )

  const cardStyle = {
    background: '#fff',
    border: '1px solid #e5e7eb',
    borderRadius: 16,
    padding: 20,
    boxShadow: '0 4px 14px rgba(15,23,42,0.04)'
  }

  return (
    <section style={{ margin: '24px 0', color: '#172033' }}>
      <div style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        flexWrap: 'wrap',
        gap: 12,
        marginBottom: 22
      }}>
        <div>
          <h1 style={{ margin: 0, fontSize: 28 }}>
            Admin Dashboard
          </h1>
          <p style={{ color: '#64748b', marginBottom: 0 }}>
            Welcome back! Here is your SmartCart overview.
          </p>
        </div>
        <span style={{
          background: '#e8f2ff',
          color: '#1d4ed8',
          borderRadius: 20,
          padding: '8px 14px',
          fontSize: 13
        }}>
          📈 Business Overview
        </span>
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(190px, 1fr))',
        gap: 16,
        marginBottom: 22
      }}>
        {[
          {
            label: 'Total Customers',
            value: customerCount,
            icon: '👥',
            color: '#2563eb',
            bg: '#eff6ff'
          },
          {
            label: 'Total Products',
            value: products.length,
            icon: '📦',
            color: '#7c3aed',
            bg: '#f5f3ff'
          },
          {
            label: 'Total Orders',
            value: orders.length,
            icon: '🛍️',
            color: '#0f766e',
            bg: '#f0fdfa'
          },
          {
            label: 'Recorded Order Value',
            value: money(totalOrderValue),
            icon: '₹',
            color: '#c2410c',
            bg: '#fff7ed'
          }
        ].map(card => (
          <div
  key={card.label}
  className="dashboardGlowCard"
  style={cardStyle}
>
            <div style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center'
            }}>
              <span style={{ color: '#64748b', fontSize: 13 }}>
                {card.label}
              </span>
              <span style={{
                background: card.bg,
                color: card.color,
                borderRadius: 12,
                padding: 10,
                fontSize: 20
              }}>
                {card.icon}
              </span>
            </div>
            <h2 style={{
              fontSize: 27,
              margin: '16px 0 4px',
              color: card.color,
              overflowWrap: 'anywhere'
            }}>
              {card.value}
            </h2>
            <span style={{ color: '#64748b', fontSize: 12 }}>
              Current recorded data
            </span>
          </div>
        ))}
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
        gap: 18,
        marginBottom: 22
      }}>
        <div style={cardStyle}>
          <h3 style={{ marginTop: 0 }}>📊 Monthly Order Value</h3>
          <p style={{ color: '#64748b', fontSize: 13 }}>
            Based on order dates and recorded totals.
          </p>

          <div style={{
            height: 210,
            display: 'flex',
            alignItems: 'flex-end',
            justifyContent: 'space-around',
            gap: 10,
            paddingTop: 14
          }}>
            {monthlySales.map(m => (
              <div key={m.label} style={{
                flex: 1,
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'flex-end',
                alignItems: 'center',
                gap: 8,
                minWidth: 0
              }}>
                <small style={{
                  fontSize: 10,
                  color: '#64748b',
                  textAlign: 'center',
                  overflowWrap: 'anywhere'
                }}>
                  {m.value ? money(m.value) : '₹0'}
                </small>
                <div style={{
                  width: '70%',
                  minWidth: 12,
                  height: `${Math.max(5, (m.value / maxSales) * 100)}%`,
                  background: 'linear-gradient(180deg, #60a5fa, #2563eb)',
                  borderRadius: '7px 7px 2px 2px'
                }} />
                <small style={{ color: '#64748b' }}>{m.label}</small>
              </div>
            ))}
          </div>
        </div>

        <div style={cardStyle}>
          <h3 style={{ marginTop: 0 }}>⚠️ Low Stock Alerts</h3>
          <p style={{ color: '#64748b', fontSize: 13 }}>
            Inventory quantity is 5 or below.
          </p>

          {lowStockItems.length === 0 ? (
            <div style={{
              background: '#ecfdf5',
              color: '#047857',
              borderRadius: 12,
              padding: 18,
              marginTop: 18
            }}>
              ✓ No low-stock inventory records.
            </div>
          ) : (
            <div style={{
              display: 'grid',
              gap: 10,
              marginTop: 14
            }}>
              {lowStockItems.slice(0, 6).map(item => (
                <div key={item.id} style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  gap: 12,
                  alignItems: 'center',
                  padding: 12,
                  background: '#fff7ed',
                  borderRadius: 10
                }}>
                  <div>
                    <b>{getProductName(item.productId)}</b>
                    <div style={{
                      fontSize: 12,
                      color: '#9a3412',
                      marginTop: 4
                    }}>
                      {item.warehouseLocation || 'Location not specified'}
                    </div>
                  </div>
                  <b style={{ color: '#c2410c' }}>
                    {item.quantity} left
                  </b>
                </div>
              ))}
            </div>
          )}

          {lowStockItems.length > 6 && (
            <p style={{ color: '#64748b', fontSize: 12 }}>
              And {lowStockItems.length - 6} more low-stock records.
            </p>
          )}

          <button
            className="primary"
            style={{ marginTop: 14 }}
            onClick={() => setTab('inventory')}
          >
            Manage Inventory →
          </button>
        </div>
      </div>

      <div style={cardStyle}>
        <h3 style={{ marginTop: 0 }}>🧾 Recent Orders</h3>

        {!recentOrders.length ? (
          <p style={{ color: '#64748b' }}>
            No orders have been recorded yet.
          </p>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={{
              width: '100%',
              borderCollapse: 'collapse',
              textAlign: 'left'
            }}>
              <thead>
                <tr style={{
                  background: '#f8fafc',
                  color: '#475569'
                }}>
                  <th style={{ padding: 12 }}>Order ID</th>
                  <th style={{ padding: 12 }}>Date</th>
                  <th style={{ padding: 12 }}>Amount</th>
                  <th style={{ padding: 12 }}>Status</th>
                </tr>
              </thead>
              <tbody>
                {recentOrders.map(o => (
                  <tr key={o.id} style={{
                    borderBottom: '1px solid #e5e7eb'
                  }}>
                    <td style={{ padding: 12 }}>#{o.id}</td>
                    <td style={{ padding: 12 }}>
                      {o.orderDate
                        ? new Date(o.orderDate).toLocaleDateString('en-IN')
                        : '—'}
                    </td>
                    <td style={{ padding: 12 }}>
                      {money(o.totalAmount)}
                    </td>
                    <td style={{ padding: 12 }}>
                      <span style={{
                        background: '#eff6ff',
                        color: '#1d4ed8',
                        padding: '5px 9px',
                        borderRadius: 20,
                        fontSize: 12
                      }}>
                        {o.status || 'UNKNOWN'}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        <button
          className="primary"
          style={{ marginTop: 16 }}
          onClick={() => setTab('orders')}
        >
          View All Orders →
        </button>
      </div>
    </section>
  )
})()}
      {/* ================= PRODUCTS ================= */}

      {tab === 'products' && (
        <>
          <form
            className="adminForm"
            onSubmit={saveProduct}
          >

            <h3>
              {editing ? 'Edit Product' : 'Add Product'}
            </h3>

            <input
              placeholder="Product name"
              required
              value={form.productName}
              onChange={e =>
                setForm({
                  ...form,
                  productName: e.target.value
                })
              }
            />

            <input
              placeholder="Description"
              required
              value={form.description}
              onChange={e =>
                setForm({
                  ...form,
                  description: e.target.value
                })
              }
            />

            <input
              type="number"
              placeholder="Price"
              required
              value={form.price}
              onChange={e =>
                setForm({
                  ...form,
                  price: e.target.value
                })
              }
            />

            <input
              type="number"
              placeholder="Stock"
              required
              value={form.stock}
              onChange={e =>
                setForm({
                  ...form,
                  stock: e.target.value
                })
              }
            />

            <input
              placeholder="Image URL"
              required
              value={form.imgURL}
              onChange={e =>
                setForm({
                  ...form,
                  imgURL: e.target.value
                })
              }
            />

            <select
              required
              value={form.categoryId}
              onChange={e =>
                setForm({
                  ...form,
                  categoryId: e.target.value
                })
              }
            >
              <option value="">
                Select category
              </option>

              {categories.map(c => (
                <option
                  key={c.id}
                  value={c.id}
                >
                  {c.categoryName}
                </option>
              ))}
            </select>

            <button className="primary">
              {editing ? 'Update' : 'Add Product'}
            </button>

            {editing && (
              <button
                type="button"
                onClick={() => {
                  setEditing(null)

                  setForm({
                    productName: '',
                    description: '',
                    price: '',
                    stock: '',
                    imgURL: '',
                    categoryId: ''
                  })
                }}
              >
                Cancel
              </button>
            )}

          </form>

          <div className="adminList">

            {products.map(p => (
              <div
                className="adminItem"
                key={p.id}
              >

                <div>
                  <b>{p.productName}</b>

                  <span>
                    {money(p.price)} • Stock {p.stock}
                  </span>
                </div>

                <div>

                  <button
                    onClick={() => {
                      setEditing(p.id)

                      setForm({
                        productName: p.productName,
                        description: p.description,
                        price: p.price,
                        stock: p.stock,
                        imgURL: p.imgURL,
                        categoryId: p.categoryId
                      })
                    }}
                  >
                    Edit
                  </button>

                  <button
                    className="danger"
                    onClick={() =>
                      removeProduct(p.id)
                    }
                  >
                    Delete
                  </button>

                </div>

              </div>
            ))}

          </div>
        </>
      )}

      {/* ================= CATEGORIES ================= */}

      {tab === 'categories' && (
        <>
          <form
            className="adminForm"
            onSubmit={saveCat}
          >

            <h3>Add Category</h3>

            <input
              placeholder="Category name"
              required
              value={catForm.categoryName}
              onChange={e =>
                setCatForm({
                  ...catForm,
                  categoryName: e.target.value
                })
              }
            />

            <input
              placeholder="Description"
              required
              value={catForm.description}
              onChange={e =>
                setCatForm({
                  ...catForm,
                  description: e.target.value
                })
              }
            />

            <button className="primary">
              Add Category
            </button>

          </form>

          <div className="adminList">

            {categories.map(c => (
              <div
                className="adminItem"
                key={c.id}
              >
                <b>{c.categoryName}</b>
                <span>{c.description}</span>
              </div>
            ))}

          </div>
        </>
      )}

      {/* ================= INVENTORY ================= */}

      {tab === 'inventory' && (
        <>
          <div className="titleRow">
            <div>
              <h2>Inventory Management</h2>
              <p>
                Manage product stock and warehouse location.
              </p>
            </div>
          </div>

          <form
            className="adminForm"
            onSubmit={saveInventory}
          >

            <h3>
              {inventoryEditing
                ? 'Update Inventory'
                : 'Add Inventory'}
            </h3>

            <select
              required
              value={inventoryForm.productId}
              onChange={e =>
                setInventoryForm({
                  ...inventoryForm,
                  productId: e.target.value
                })
              }
            >
              <option value="">
                Select Product
              </option>

              {products.map(p => (
                <option
                  key={p.id}
                  value={p.id}
                >
                  {p.productName}
                </option>
              ))}
            </select>

            <input
              type="number"
              min="0"
              placeholder="Available Quantity"
              required
              value={inventoryForm.quantity}
              onChange={e =>
                setInventoryForm({
                  ...inventoryForm,
                  quantity: e.target.value
                })
              }
            />

            <input
              placeholder="Warehouse Location"
              value={inventoryForm.warehouseLocation}
              onChange={e =>
                setInventoryForm({
                  ...inventoryForm,
                  warehouseLocation: e.target.value
                })
              }
            />

            <button className="primary">
              {inventoryEditing
                ? 'Update Inventory'
                : 'Add Inventory'}
            </button>

            {inventoryEditing && (
              <button
                type="button"
                onClick={resetInventoryForm}
              >
                Cancel
              </button>
            )}

          </form>

          {/* INVENTORY LIST */}

          <div className="adminList">

            {!inventory.length && (
              <div className="empty">
                No inventory records found.
              </div>
            )}

            {inventory.map(item => {

              const quantity = Number(
                item.quantity || 0
              )

              return (
                <div
                  className="adminItem"
                  key={item.id}
                >

                  <div>
                    <b>
                      {getProductName(item.productId)}
                    </b>

                    <span>
                      Quantity: {quantity}
                    </span>

                    <span>
                      Warehouse:{' '}
                      {item.warehouseLocation || 'Not specified'}
                    </span>

                    {quantity === 0 && (
                      <span className="dangerText">
                        Out of Stock
                      </span>
                    )}

                    {quantity > 0 && quantity <= 5 && (
                      <span
                        style={{
                          color: '#b45309',
                          fontSize: '13px'
                        }}
                      >
                        Low Stock
                      </span>
                    )}

                    {quantity > 5 && (
                      <span
                        style={{
                          color: '#047857',
                          fontSize: '13px'
                        }}
                      >
                        In Stock
                      </span>
                    )}

                  </div>

                  <div>

                    <button
                      onClick={() =>
                        editInventory(item)
                      }
                    >
                      Edit
                    </button>

                    <button
                      className="danger"
                      onClick={() =>
                        removeInventory(item.id)
                      }
                    >
                      Delete
                    </button>

                  </div>

                </div>
              )
            })}

          </div>
        </>
      )}

      {/* ================= ORDERS ================= */}

      {tab === 'orders' && (
        <div className="orders">

          {!orders.length && (
            <div className="empty">
              No orders yet.
            </div>
          )}

          {orders.map(o => (
  <div
    className="order"
    key={o.id}
  >
    <div>
      <b>Order #{o.id}</b>

      <p>
        User ID: {o.userId || o.user?.id || '-'}
      </p>

      {o.orderDate && (
        <p>
          {new Date(o.orderDate).toLocaleString()}
        </p>
      )}
    </div>

    <strong>
      {money(o.totalAmount)}
    </strong>

    <span className="status">
      {o.status}
    </span>
  </div>
))}
        </div>
      )}

    </main>
  )
}