$(function () {
  $("#registrationForm").validate({
    rules: {
      firstName: "required",
      lastName: "required",

      nric: {
        required: true,
        nricV: true,
      },

      dateOfBirth: {
        required: true,
        minAge: true
      },

      contactNum: {
        required: true,
        contactNumberV: true,
      },

      email: "required",

      uciLicense: {
        required: function () {
          return $('input[name="hasValidUCI"]:checked').val() === "yes";
        },
        uciNumberV: true,
      },

      insuranceDeclared: {
        required: function () {
          return (
            $('input[name="hasPersonalInsurance"]:checked').val() === "yes"
          );
        },
      },

      postalCode: {
        required: function () {
            return (
                $('input[name="hasPersonalInsurance"]:checked').val() === "no" && $('input[name="hasValidUCI"]:checked').val() === "no"
            );
        },
        postalCodeV: true,
      },

      emergencyContactName: "required",

      emergencyContactNum: {
        required: true,
        contactNumberV: true,
        notSameAsEmergencyContact: true
      },
      emergencyRelation: "required",

      selectSeries: {
        required: true,
      }
    },

    messages: {
      firstName: "Please enter your first name",
      lastName: "Please enter your last name",

      nric: {
        required: "Please enter your NRIC (last 4 characters)",
        nricV: "NRIC must follow 123A format",
      },

      dateOfBirth: {
        required: "Please enter your date of birth",
        minAge: "The minimum age for participation is 6 years old"
      },

      contactNum: {
        required: "Please enter your contact number",
        contactNumberV: "Please enter a valid Singapore number",
      },

      uciLicense: {
        uciNumberV: "Please enter a valid UCI License number OR 000",
      },

      postalCode: {
        postalCodeV: "Please enter a valid postal code",
      },

      emergencyContactName: "Please enter the name of the emergency contact person",

      emergencyContactNum: {
        required: "Please enter an emergency contact number",
        contactNumberV: "Please enter a valid Singapore number",
        notSameAsEmergencyContact: "Emergency contact number should not be the same as your contact number"
      },
    },

    highlight: function (element, errorClass, validClass) {
      $(element).addClass("error-field");
    },
    unhighlight: function (element, errorClass, validClass) {
      $(element).removeClass("error-field");
    },
  });
});

jQuery.validator.addMethod("nricV", function (value) {
  const nricRegex = /^\d{3}[a-zA-Z]$/;
  return nricRegex.test(value);
});

jQuery.validator.addMethod("contactNumberV", function (value) {
  const sgHPRegex = /^[89]\d{7}$/;
  return sgHPRegex.test(value);
});

jQuery.validator.addMethod("uciNumberV", function (value, element) {
  const uciRegex = /^(000|\d{11})$/;
  return this.optional(element) || uciRegex.test(value);
});

jQuery.validator.addMethod("notSameAsEmergencyContact", function (value) {
    let contactNum = $("#contactNum").val();
    return value !== contactNum;
});

jQuery.validator.addMethod("minAge", function (value) {
    const MIN_AGE_TO_PARTICIPATE = 6;

    let current_year = new Date().getFullYear().toString();
    let birth_year = value.split('-')[0];
    let age = current_year - birth_year;
    return age >= MIN_AGE_TO_PARTICIPATE;
});

jQuery.validator.addMethod("beforeToday", function (value) {
    let todayDate = new Date().toISOString().split('T')[0];

    const parts = value.split('-');
    const year = parseInt(parts[0]);
    const month = parseInt(parts[1]) - 1; // JavaScript months are zero-based (0 for January)
    const day = parseInt(parts[2]);

    const dob = new Date(year, month, day); 
    return dob < todayDate;
}); 

jQuery.validator.addMethod("postalCodeV", function (value, element) {
    const postalCodeRegex = /^[0-9]{6}$/;
    return this.optional(element) || postalCodeRegex.test(value);
});