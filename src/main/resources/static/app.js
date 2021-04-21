//import Vue from 'vue'

Vue.component("loginForm",{
    el: "#loginForm",
    data: {password: ""},
    methods: {
        onLoginSubmit: function () {

            console.log(this.password);

            axios.post("/login", {
                password2: this.password,
            }).then(function () {

                console.log(arguments);

            }).catch(function () {
                alert("ошибка:запрос не обработан");
                console.log(arguments);
            });
        }
    }
});

const app = new Vue({
    el: '#app',
});