Vue.component('component-name', {
    props: ['player'],
    template: '<div>' +
        '{{player.name}} {{player.id}}' +
        '</div>'
});

var app = new Vue({
    el: '#contents',
    data: {
        players: [
            {
                id: "1",
                name: "name1",
            },
            {
                id: "2",
                name: "name2",
            }
        ]
    }
});


console.log('tes23t');