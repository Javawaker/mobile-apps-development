$(document).ready(function() {
    $('#boilingForm').on('submit', function(e) {
        e.preventDefault();
        var liquidName = $('#liquidName').val();
        var volume = $('#volume').val();
        var heaterWattage = $('#heaterWattage').val();

        $.ajax({
            url: 'calculate.php',
            type: 'GET',
            dataType: 'json',
            data: { liquidName, volume, heaterWattage },
            success: function(response) {
                console.log(response);
                if(response.error == false)
                {
                    $('#result').html('Время до кипения: ' + response.timeToBoil + ' секунд<br>' +
                                    'Теплоемкость: ' + response.heatCapacity + ' Дж/(кг·К)<br>' +
                                    'Температура кипения: ' + response.boilingTemperature + ' °C');
                } else {
                    $('#result').html('Ошибка: ' + response.error);
                }},
            error: function(xhr, status, error) {
                console.log(data);
                $('#result').html('Ошибка: ' + error);
            }
        });
    });
});