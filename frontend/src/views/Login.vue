const handleSubmit = async () => { try { await userStore.login(form) const redirectPath =
route.query.redirect || '/' router.push(redirectPath) } catch (error) { // ... 에러 처리 ... } }
