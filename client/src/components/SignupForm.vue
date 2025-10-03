<template>
  <h1 class="mt-3">
    Sign <span style="font-family: 'Arial Black', serif">Up</span>
  </h1>
  <form
    class="m-auto mt-2 p-3"
    style="max-width: 400px"
    @submit.prevent="signup()"
  >
    <div class="mb-2 text-start">
      <label for="name" class="form-label">Name / Organization</label>
      <input
        type="text"
        class="form-control"
        id="name"
        v-model="signupForm.name"
        required
      />
    </div>
    <div class="mb-2 text-start">
      <label for="surname" class="form-label">Surname (optional)</label>
      <input
        type="text"
        class="form-control"
        id="surname"
        v-model="signupForm.surname"
      />
    </div>
    <div class="mb-2 text-start">
      <label for="email" class="form-label">Email Address</label>
      <input
        type="text"
        class="form-control"
        id="email"
        v-model="signupForm.email"
        required
        minlength="4"
      />
    </div>
    <div class="mb-2 text-start">
      <label for="password" class="form-label">Password</label>
      <input
        type="password"
        class="form-control"
        id="password"
        v-model="signupForm.password"
        required="required"
        minlength="4"
      />
    </div>
    <button
      type="submit"
      class="btn btn-primary mt-2"
      @click="
        signupForm.success = false;
        signupForm.error = null;
      "
    >
      Submit
    </button>
    <div
      class="alert alert-success alert-dismissible fade show mt-3"
      role="alert"
      v-if="signupForm.success"
    >
      Account created
      <button
        type="button"
        class="btn-close"
        data-bs-dismiss="alert"
        aria-label="Close"
        @click="signupForm.success = false"
      ></button>
    </div>
    <div
      class="alert alert-danger alert-dismissible fade show mt-3"
      role="alert"
      v-if="signupForm.error"
    >
      <strong>Error:</strong> {{ signupForm.error }}
      <button
        type="button"
        class="btn-close"
        data-bs-dismiss="alert"
        aria-label="Close"
        @click="signupForm.error = null"
      ></button>
    </div>
  </form>
</template>

<script>
import auth from "@/common/auth";

export default {
  data() {
    return {
      signupForm: {
        name: null,
        surname: null,
        email: null,
        password: null,
        success: false,
        error: null,
      },
    };
  },
  methods: {
    async signup() {
      try {
        await auth.signup(this.signupForm);
        this.signupForm.success = true;
        //this.$router.go(-1);
      } catch (err) {
        const response = JSON.parse(err.request.response);
        this.signupForm.error = response.message;
        console.error(err);
      }
    },
  },
};
</script>
