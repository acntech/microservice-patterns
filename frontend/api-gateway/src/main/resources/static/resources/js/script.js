$('tr.user').click(function () {
    const username = $(this).find('td.username').html();
    $('input#username').val(username);
});