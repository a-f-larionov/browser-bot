//import Vue from 'vue'


var app = new Vue({
    el: '#app',
    data: {
        password: "",
    },
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


console.log('tes23t');