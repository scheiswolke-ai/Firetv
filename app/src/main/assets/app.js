(() => {
 const $=s=>document.querySelector(s), toast=$('#toast'), dialog=$('#settings');
 let timer;
 function say(t){toast.textContent=t;toast.classList.add('show');clearTimeout(timer);timer=setTimeout(()=>toast.classList.remove('show'),2300)}
 function tick(){const d=new Date();$('#clock').textContent=d.toLocaleTimeString('de-DE',{hour:'2-digit',minute:'2-digit'});$('#date').textContent=d.toLocaleDateString('de-DE',{weekday:'short',day:'2-digit',month:'short'});$('#greeting').textContent=d.getHours()<11?'GUTEN MORGEN':d.getHours()<18?'GUTEN TAG':'GUTEN ABEND'}
 tick();setInterval(tick,1000);
 document.querySelectorAll('[data-app]').forEach(b=>b.addEventListener('click',()=>{const ok=window.FireTV?.launchApp(b.dataset.app);if(!ok)say('App nicht gefunden oder nicht startbar: '+b.dataset.app)}));
 $('#settingsBtn').onclick=()=>dialog.showModal();$('#close').onclick=()=>dialog.close();
 $('#explore').onclick=()=>$('#apps').scrollIntoView({behavior:'smooth'});
 document.querySelectorAll('[data-theme]').forEach(b=>b.onclick=()=>{document.body.dataset.theme=b.dataset.theme;say('Design: '+b.dataset.theme)});
 document.querySelectorAll('.nav').forEach(b=>b.onclick=()=>{document.querySelectorAll('.nav').forEach(x=>x.classList.remove('active'));b.classList.add('active');if(b.dataset.target==='home')window.scrollTo(0,0);else if(b.dataset.target==='apps')$('#apps').scrollIntoView({behavior:'smooth'});else say('Dieser Bereich folgt in einem Update.')});
 // Remote-friendly focus movement: move among visible buttons in DOM order.
 document.addEventListener('keydown',e=>{if(!['ArrowRight','ArrowLeft','ArrowUp','ArrowDown'].includes(e.key))return;const list=[...document.querySelectorAll('button')].filter(x=>x.getClientRects().length&&!x.disabled);const i=list.indexOf(document.activeElement);if(i<0){list[0]?.focus();e.preventDefault();return}const step=(e.key==='ArrowRight'||e.key==='ArrowDown')?1:-1;list[Math.max(0,Math.min(list.length-1,i+step))]?.focus();e.preventDefault()});
})();